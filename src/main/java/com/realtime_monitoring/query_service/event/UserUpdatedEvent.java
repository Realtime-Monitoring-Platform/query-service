package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record UserUpdatedEvent(
    DomainEvent event,
     UUID id,
     String username,
     String email,
     String firstName,
     String lastName,
     String phone,
     String avatarUrl,
     String password,
     String Address,
     String status,
     UUID tenantId,
     UUID roleId,
     UUID teamId
) {}
