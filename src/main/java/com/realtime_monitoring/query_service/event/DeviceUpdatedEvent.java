package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record DeviceUpdatedEvent(
    DomainEvent event,
    UUID tenantId,
    UUID teamId,
    UUID assignedUserId,
    String deviceName,
    String model,
    String manufacturer,
    String hostname,
    String ipAddress,
    String macAddress,
    String location,
    String status
) {}
