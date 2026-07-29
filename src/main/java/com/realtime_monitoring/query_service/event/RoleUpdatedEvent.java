package com.realtime_monitoring.user_manag.kafka.event;

import java.util.UUID;

public record RoleUpdatedEvent(
    DomainEvent event,
    UUID roleId,
    String name,
    String description
) {
}
