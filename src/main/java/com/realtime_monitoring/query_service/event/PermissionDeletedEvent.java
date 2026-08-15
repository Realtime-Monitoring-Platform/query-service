package com.realtime_monitoring.query_service.event;

import java.util.UUID;

public record PermissionDeletedEvent(
    DomainEvent event,
    UUID permissionId
) {
}