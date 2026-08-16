package com.realtime_monitoring.query_service.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.realtime_monitoring.query_service.dto.MetricsDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceMetricsQueryService {

    private final InfluxDBClient influxDBClient;

    @Value("${influxdb.org:docs}")
    private String org;

    public DeviceMetricsQueryService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public List<MetricsDTO> getMetrics(
            String tenantId,
            String deviceId,
            String startTime
    ) {
        // Default to -24h if null/blank
        String rangeStart = (startTime != null && !startTime.isBlank()) ? startTime : "-24h";

        // Format relative durations (-24h, -1h) vs ISO strings (2026-08-16T10:00:00Z)
        String rangeExpression = rangeStart.startsWith("-") 
                ? rangeStart 
                : "time(v: \"" + rangeStart + "\")";

        // Safely format the Flux script directly
        String flux = """
                from(bucket: "chedly")
                  |> range(start: %s)
                  |> filter(fn: (r) => r._measurement == "device_metrics")
                  |> filter(fn: (r) => r.device_id == "%s")
                  |> filter(fn: (r) => r.tenant_id == "%s")
                  |> pivot(
                      rowKey: ["_time"],
                      columnKey: ["_field"],
                      valueColumn: "_value"
                  )
                """.formatted(rangeExpression, deviceId, tenantId);

        System.out.println("Executing Flux query:\n" + flux);

        List<FluxTable> tables = influxDBClient
                .getQueryApi()
                .query(flux, org);

        List<MetricsDTO> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {

                Double cpu = record.getValueByKey("cpu") instanceof Number
                        ? ((Number) record.getValueByKey("cpu")).doubleValue()
                        : null;

                Double ram = record.getValueByKey("ram") instanceof Number
                        ? ((Number) record.getValueByKey("ram")).doubleValue()
                        : null;

                MetricsDTO metricsDTO = new MetricsDTO();
                metricsDTO.setDevice_id(UUID.fromString(deviceId));
                metricsDTO.setTenant_id(UUID.fromString(tenantId));
                metricsDTO.setTimestamp(record.getTime());
                metricsDTO.setCpu(cpu);
                metricsDTO.setRam(ram);

                result.add(metricsDTO);
            }
        }

        return result;
    }
}