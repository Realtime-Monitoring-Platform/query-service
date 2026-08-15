package com.realtime_monitoring.query_service.dto;

import com.realtime_monitoring.query_service.model.RoleOverview;
import com.realtime_monitoring.query_service.model.PermissionOverview;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

public record RoleOverviewResponse(
    UUID id,
    String name,
    String description,
    int userNumber,
    int permissionNumber,
    java.util.Set<PermissionOverviewResponse> permissions,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static RoleOverviewResponse from(RoleOverview role) {
        java.util.Set<PermissionOverviewResponse> permResponses = role.getPermissions() == null ? java.util.Collections.emptySet() :
            role.getPermissions().stream()
                .map(PermissionOverviewResponse::from)
                .collect(Collectors.toSet());
        
        return new RoleOverviewResponse(
            role.getId(),
            role.getName(),
            role.getDescription(),
            role.getUserNumber(),
            role.getPermissionNumber(),
            permResponses,
            role.getCreatedAt(),
            role.getUpdatedAt()
        );
    }
}