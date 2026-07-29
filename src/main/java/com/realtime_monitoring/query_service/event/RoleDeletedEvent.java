package com.realtime_monitoring.user_manag.kafka.event;

import java.util.UUID;

public record RoleDeletedEvent(
    DomainEvent event,
        UUID id
) {}


