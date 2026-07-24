package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record TenantUpdatedEvent(
    UUID id,
    DomainEvent event,
    String name,
    String companyName,
    String email,
    String phone,
    String status
) {}
