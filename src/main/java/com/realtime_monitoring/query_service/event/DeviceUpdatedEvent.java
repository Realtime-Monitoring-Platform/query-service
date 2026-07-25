package com.realtime_monitoring.query_service.event;

import java.time.LocalDateTime;
import java.util.UUID;


public record DeviceUpdatedEvent(
        UUID id,
        DomainEvent event,
        UUID assignedUserId,
        String deviceName,
        String model,
        String manufacturer,
        String hostname,
        String ipAddress,
        String macAddress,
        String location,
        String status,
        LocalDateTime lastSeen,
        UUID tenantId,
        UUID teamId
) {
}
