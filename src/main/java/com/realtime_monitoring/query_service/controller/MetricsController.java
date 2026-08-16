package com.realtime_monitoring.query_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.influxdb.client.service.MetricsService;
import com.realtime_monitoring.query_service.dto.MetricsDTO;
import com.realtime_monitoring.query_service.service.DeviceMetricsQueryService;

import java.util.List;

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

    @GetMapping("/deviceid")
    public List<MetricsDTO> getMetricsByDeviceId(@RequestParam String deviceId) {
        return this.metricsService.getMetricsByDeviceId(deviceId);
    }
    
    
}
