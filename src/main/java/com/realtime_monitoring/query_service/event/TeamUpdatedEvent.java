package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record TeamUpdatedEvent(
    BaseEvent event,
    String name,
    String description,
    UUID tenantId
) {}
