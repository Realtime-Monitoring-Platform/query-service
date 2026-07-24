package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record UserCreatedEvent(
    BaseEvent event,
    String username,
    String email,
    String firstName,
    String lastName,
    String phone,
    String avatarUrl,
    String address,
    UUID tenantId,
    UUID teamId,
    String status
) {}
