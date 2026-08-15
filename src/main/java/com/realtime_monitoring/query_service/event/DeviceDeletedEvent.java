package com.realtime_monitoring.query_service.event;
import java.util.UUID;

public record DeviceDeletedEvent(
    DomainEvent event,
        UUID id
) {}
