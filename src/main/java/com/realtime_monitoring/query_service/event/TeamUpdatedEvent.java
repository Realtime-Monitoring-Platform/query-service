package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record TeamUpdatedEvent(
    DomainEvent event,
    String name,
    String description,
    UUID tenantId
) {}
