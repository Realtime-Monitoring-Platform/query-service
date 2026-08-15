package com.realtime_monitoring.query_service.dto;

import com.realtime_monitoring.query_service.model.PermissionOverview;

import java.time.LocalDateTime;
import java.util.UUID;

public record PermissionOverviewResponse(
    UUID id,
    String name,
    String description,
    String resource,
    String action,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PermissionOverviewResponse from(PermissionOverview permission) {
        return new PermissionOverviewResponse(
            permission.getId(),
            permission.getName(),
            permission.getDescription(),
            permission.getResource(),
            permission.getAction(),
            permission.getCreatedAt(),
            permission.getUpdatedAt()
        );
    }
}