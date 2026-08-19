package com.realtime_monitoring.query_service.dto;

import com.realtime_monitoring.query_service.model.DeviceOverview;
import com.realtime_monitoring.query_service.model.DeviceStatusOverview;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceOverviewResponse(
    UUID id,
    UUID tenantId,
    String tenantName,
    UUID teamId,
    String teamName,
    UUID assignedUserId,
    String assignedUserName,
    String deviceName,
    String model,
    String manufacturer,
    String hostname,
    String ipAddress,
    String macAddress,
    String location,
     
    DeviceStatusOverview status,
    LocalDateTime lastSeen,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String osName,
     String osVersion,
     String kernelVersion,
     Long cpuCount,
     Long totalMemoryKb
) {
    public static DeviceOverviewResponse from(DeviceOverview device) {
        return new DeviceOverviewResponse(
            device.getId(),
            device.getTenantId(),
            device.getTenantName(),
            device.getTeamId(),
            device.getTeamName(),
            device.getAssignedUserId(),
            device.getAssignedUserName(),
            device.getDeviceName(),
            device.getModel(),
            device.getManufacturer(),
            device.getHostname(),
            device.getIpAddress(),
            device.getMacAddress(),
            device.getLocation(),
            device.getStatus(),
            device.getLastSeen(),
            device.getCreatedAt(),
            device.getUpdatedAt(),
            device.getOsName(),
            device.getOsVersion(),
            device.getKernelVersion(),
            device.getCpuCount(),
            device.getTotalMemoryKb()
            
        );
    }
}
