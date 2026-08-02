package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.dto.DeviceOverviewResponse;
import com.realtime_monitoring.query_service.service.DeviceQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/devices")
public class DeviceQueryController {

    private final DeviceQueryService deviceQueryService;

    public DeviceQueryController(DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceOverviewResponse> getDeviceById(@PathVariable UUID id) {
        return deviceQueryService.getDeviceById(id)
                .map(DeviceOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<DeviceOverviewResponse>> getAllDevices(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<DeviceOverviewResponse> devices = deviceQueryService.getAllDevices(pageable)
                .map(DeviceOverviewResponse::from);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<Page<DeviceOverviewResponse>> getDevicesByTenant(
            @PathVariable UUID tenantId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<DeviceOverviewResponse> devices = deviceQueryService.getDevicesByTenant(tenantId, pageable)
                .map(DeviceOverviewResponse::from);
        return ResponseEntity.ok(devices);
    }
}
