package com.realtime_monitoring.query_service.service;

import java.util.UUID;

public interface ProjectionService {
    boolean isEventProcessed(UUID eventId);
    void markEventAsProcessed(UUID eventId, String eventType, String aggregateId);
}
