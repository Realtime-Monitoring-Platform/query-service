package com.realtime_monitoring.query_service.event;

import java.time.Instant;
import java.util.UUID;

public record BaseEvent(
    UUID eventId,
    String eventType,
    UUID aggregateId,
    String aggregateType,
    Instant occurredAt
) {}
