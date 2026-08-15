package com.realtime_monitoring.query_service.event;

import java.util.Set;
import java.util.UUID;

public record RoleCreatedEvent(
    DomainEvent event,
    UUID roleId,
    String name,
    String description,
    Set<UUID> permissionIds
) {
}
