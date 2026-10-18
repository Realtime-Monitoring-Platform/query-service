package com.realtime_monitoring.query_service.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.realtime_monitoring.query_service.dto.DiskMetricsDTO;
import com.realtime_monitoring.query_service.dto.MemoryMetricsDTO;
import com.realtime_monitoring.query_service.dto.MetricsDTO;
import com.realtime_monitoring.query_service.dto.MetricsSnapshotDTO;
import com.realtime_monitoring.query_service.dto.NetworkMetricsDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DeviceMetricsQueryService {

    /** Scalar series written by the device-management service (cpu, ram, memory_*). */
    private static final String METRICS_MEASUREMENT = "device_metrics";
    /** One point per disk/partition, tagged with mount_point / name / file_system. */
    private static final String DISK_MEASUREMENT = "device_disk_metrics";
    /** One point per network interface, tagged with interface. */
    private static final String NETWORK_MEASUREMENT = "device_network_metrics";

    private final InfluxDBClient influxDBClient;

    @Value("${influxdb.org:docs}")
    private String org;

    @Value("${influxdb.bucket:chedly}")
    private String bucket;

    public DeviceMetricsQueryService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public List<MetricsDTO> getMetrics(
            String tenantId,
            String deviceId,
            String startTime) {

        return loadMetrics(rangeExpression(startTime), deviceId, tenantId, false);
    }

    public List<MetricsDTO> getMetricsByDeviceId(String deviceId, String startTime) {
        return loadMetrics(rangeExpression(startTime), deviceId, null, false);
    }

    /**
     * Latest reading of a device: the most recent cpu/ram/memory sample enriched with the
     * disks and the network interfaces collected by the agent in the very same metrics message.
     *
     * @return the snapshot or {@code null} when the device never reported any metric
     */
    public MetricsSnapshotDTO getLatestMetrics(String deviceId, String startTime) {
        List<MetricsDTO> latest = loadMetrics(rangeExpression(startTime), deviceId, null, true);

        if (latest.isEmpty()) {
            return null;
        }

        MetricsDTO row = latest.get(0);

        MetricsSnapshotDTO snapshot = new MetricsSnapshotDTO();
        snapshot.setDevice_id(row.getDevice_id());
        snapshot.setTenant_id(row.getTenant_id());
        snapshot.setTimestamp(row.getTimestamp());
        snapshot.setCpu(row.getCpu());
        snapshot.setRam(row.getRam());
        snapshot.setMemory(row.getMemory());
        snapshot.setDisks(loadDisksAt(deviceId, row.getTimestamp()));
        snapshot.setNetwork(loadNetworkAt(deviceId, row.getTimestamp()));

        return snapshot;
    }

    private List<MetricsDTO> loadMetrics(
            String rangeExpression,
            String deviceId,
            String tenantId,
            boolean latestOnly) {

        String flux = scalarFlux(rangeExpression, deviceId, tenantId, latestOnly);

        System.out.println("Executing Flux query:\n" + flux);

        List<FluxTable> tables = influxDBClient
                .getQueryApi()
                .query(flux, org);

        List<MetricsDTO> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                result.add(toMetrics(record, deviceId, tenantId));
            }
        }

        return result;
    }

    private MetricsDTO toMetrics(FluxRecord record, String deviceId, String tenantId) {
        MetricsDTO metricsDTO = new MetricsDTO();
        metricsDTO.setDevice_id(UUID.fromString(deviceId));
        metricsDTO.setTenant_id(resolveTenantId(record, tenantId));
        metricsDTO.setTimestamp(record.getTime());
        metricsDTO.setCpu(asDouble(record.getValueByKey("cpu"), 0d));
        metricsDTO.setRam(asDouble(record.getValueByKey("ram"), 0d));

        MemoryMetricsDTO memory = new MemoryMetricsDTO();
        memory.setTotalKb(asLong(record.getValueByKey("memory_total_kb")));
        memory.setUsedKb(asLong(record.getValueByKey("memory_used_kb")));
        memory.setFreeKb(asLong(record.getValueByKey("memory_free_kb")));
        memory.setAvailableKb(asLong(record.getValueByKey("memory_available_kb")));
        memory.setSwapTotalKb(asLong(record.getValueByKey("memory_swap_total_kb")));
        memory.setSwapUsedKb(asLong(record.getValueByKey("memory_swap_used_kb")));

        if (memory.getTotalKb() != null
                || memory.getUsedKb() != null
                || memory.getAvailableKb() != null
                || memory.getSwapTotalKb() != null) {
            metricsDTO.setMemory(memory);
        }

        return metricsDTO;
    }

    private List<DiskMetricsDTO> loadDisksAt(String deviceId, Instant timestamp) {
        Map<String, DiskMetricsDTO> disks = new LinkedHashMap<>();

        for (FluxRecord record : queryRaw(detailFlux(DISK_MEASUREMENT, deviceId, timestamp))) {
            String mountPoint = stringValue(record, "mount_point");
            DiskMetricsDTO disk = disks.computeIfAbsent(mountPoint, key -> new DiskMetricsDTO());
            disk.setMountPoint(mountPoint);
            disk.setName(stringValue(record, "name"));
            disk.setFileSystem(stringValue(record, "file_system"));

            switch (record.getField()) {
                case "total_bytes" -> disk.setTotalBytes(asLong(record.getValue()));
                case "available_bytes" -> disk.setAvailableBytes(asLong(record.getValue()));
                case "used_bytes" -> disk.setUsedBytes(asLong(record.getValue()));
                case "used_percent" -> disk.setUsedPercent(asDouble(record.getValue(), null));
                case "read_bytes" -> disk.setReadBytes(asLong(record.getValue()));
                case "written_bytes" -> disk.setWrittenBytes(asLong(record.getValue()));
                default -> { /* ignore unknown fields */ }
            }
        }

        return new ArrayList<>(disks.values());
    }

    private List<NetworkMetricsDTO> loadNetworkAt(String deviceId, Instant timestamp) {
        Map<String, NetworkMetricsDTO> interfaces = new LinkedHashMap<>();

        for (FluxRecord record : queryRaw(detailFlux(NETWORK_MEASUREMENT, deviceId, timestamp))) {
            String interfaceName = stringValue(record, "interface");
            NetworkMetricsDTO network = interfaces
                    .computeIfAbsent(interfaceName, key -> new NetworkMetricsDTO());
            network.setInterfaceName(interfaceName);

            switch (record.getField()) {
                case "received_bytes" -> network.setReceivedBytes(asLong(record.getValue()));
                case "transmitted_bytes" -> network.setTransmittedBytes(asLong(record.getValue()));
                default -> { /* ignore unknown fields */ }
            }
        }

        return new ArrayList<>(interfaces.values());
    }

    private List<FluxRecord> queryRaw(String flux) {
        System.out.println("Executing Flux query:\n" + flux);

        List<FluxRecord> records = new ArrayList<>();
        for (FluxTable table : influxDBClient.getQueryApi().query(flux, org)) {
            records.addAll(table.getRecords());
        }
        return records;
    }

    private String scalarFlux(
            String rangeExpression,
            String deviceId,
            String tenantId,
            boolean latestOnly) {

        StringBuilder flux = new StringBuilder();
        flux.append("from(bucket: \"").append(bucket).append("\")\n")
                .append("  |> range(start: ").append(rangeExpression).append(")\n")
                .append("  |> filter(fn: (r) => r._measurement == \"")
                .append(METRICS_MEASUREMENT)
                .append("\")\n")
                .append("  |> filter(fn: (r) => r.device_id == \"")
                .append(deviceId)
                .append("\")\n");

        if (tenantId != null && !tenantId.isBlank()) {
            flux.append("  |> filter(fn: (r) => r.tenant_id == \"")
                    .append(tenantId)
                    .append("\")\n");
        }

        flux.append("  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n")
                .append("  |> group()\n");

        if (latestOnly) {
            flux.append("  |> sort(columns: [\"_time\"], desc: true)\n")
                    .append("  |> limit(n: 1)");
        } else {
            flux.append("  |> sort(columns: [\"_time\"])");
        }

        return flux.toString();
    }

    /**
     * Builds the query of the disk/network measurements for one exact point in time. Both
     * measurements are written by device-management with the same timestamp as the scalar
     * metrics of the same MQTT message, so filtering on {@code _time} re-attaches them to the
     * matching cpu/ram/memory sample.
     */
    private String detailFlux(String measurement, String deviceId, Instant timestamp) {
        String time = DateTimeFormatter.ISO_INSTANT.format(timestamp);

        return "from(bucket: \"%s\")\n".formatted(bucket)
                + "  |> range(start: time(v: \"%s\"))\n".formatted(time)
                + "  |> filter(fn: (r) => r._measurement == \"%s\")\n".formatted(measurement)
                + "  |> filter(fn: (r) => r.device_id == \"%s\")\n".formatted(deviceId)
                + "  |> filter(fn: (r) => r._time == time(v: \"%s\"))".formatted(time);
    }

    private String rangeExpression(String startTime) {
        String rangeStart = (startTime != null && !startTime.isBlank() ? startTime : "-24h");

        return rangeStart.startsWith("-")
                ? rangeStart
                : "time(v: \"" + rangeStart + "\")";
    }

    private UUID resolveTenantId(FluxRecord record, String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) {
            return UUID.fromString(tenantId);
        }

        Object tagValue = record.getValueByKey("tenant_id");
        if (tagValue == null) {
            return null;
        }

        try {
            return UUID.fromString(tagValue.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static String stringValue(FluxRecord record, String key) {
        Object value = record.getValueByKey(key);
        return value == null ? null : value.toString();
    }

    private static Long asLong(Object value) {
        return value instanceof Number number ? number.longValue() : null;
    }

    private static Double asDouble(Object value, Double fallback) {
        return value instanceof Number number ? number.doubleValue() : fallback;
    }
}
