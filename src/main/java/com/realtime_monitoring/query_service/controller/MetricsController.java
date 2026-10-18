package com.realtime_monitoring.query_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.influxdb.client.service.MetricsService;
import com.realtime_monitoring.query_service.dto.MetricsDTO;
import com.realtime_monitoring.query_service.dto.MetricsSnapshotDTO;
import com.realtime_monitoring.query_service.service.DeviceMetricsQueryService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/query/metrics")
public class MetricsController {
    private final DeviceMetricsQueryService metricsService;
    public MetricsController(DeviceMetricsQueryService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public List<MetricsDTO> getMetricsByTenantId(@RequestParam String tenantId, @RequestParam String deviceId,@RequestParam String startTime ) {
        return this.metricsService.getMetrics(tenantId, deviceId,startTime);
    }

    @GetMapping("/deviceid/{deviceId}")
    public List<MetricsDTO> getMetricsByDeviceId(@PathVariable String deviceId,@RequestParam String startTime) {
        return this.metricsService.getMetricsByDeviceId(deviceId, startTime);
    }

    /**
     * Latest reading of the device: cpu/ram, memory breakdown, disks and network interfaces
     * collected in the very same metrics message published by the agent.
     */
    @GetMapping("/latest/deviceid/{deviceId}")
    public ResponseEntity<MetricsSnapshotDTO> getLatestMetrics(
            @PathVariable String deviceId,
            @RequestParam(required = false, defaultValue = "-24h") String startTime) {
        return ResponseEntity.ok(this.metricsService.getLatestMetrics(deviceId, startTime));
    }
    
    
}
