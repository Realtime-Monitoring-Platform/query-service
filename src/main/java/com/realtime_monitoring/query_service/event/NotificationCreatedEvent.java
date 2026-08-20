package com.realtime_monitoring.query_service.event;

public record NotificationCreatedEvent(
        DomainEvent event,
        String id,
        String title,
        String description,
        String type,
        String userId,
        
        String tenantId,
        long createdAt,
        boolean read
) {
}
