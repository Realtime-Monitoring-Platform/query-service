package com.realtime_monitoring.query_service.event;
import java.util.UUID;

public record TeamDeletedEvent(
    DomainEvent event,
        UUID id
) {}


