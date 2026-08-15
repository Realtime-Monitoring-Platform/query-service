package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record TenantUpdatedEvent(
    
    DomainEvent event,
    UUID id,
    String name,
    String companyName,
    String email,
    String phone,
    String status,
     UUID AdminId
) {}
