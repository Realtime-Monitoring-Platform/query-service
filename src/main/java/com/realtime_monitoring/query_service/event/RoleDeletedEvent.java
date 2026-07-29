package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record RoleDeletedEvent(
    DomainEvent event,
        UUID id
) {}


