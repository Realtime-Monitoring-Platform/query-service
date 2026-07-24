package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record TenantCreatedEvent(
    BaseEvent event,
    String name,
    String companyName,
    String email,
    String phone,
    String status
) {}
