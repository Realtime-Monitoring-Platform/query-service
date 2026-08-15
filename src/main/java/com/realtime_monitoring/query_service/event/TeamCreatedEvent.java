package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record TeamCreatedEvent(
    UUID id,
    DomainEvent event,
    String name,
    String description,
    UUID tenantId,
    UUID teamLeaderId
    

) {}
