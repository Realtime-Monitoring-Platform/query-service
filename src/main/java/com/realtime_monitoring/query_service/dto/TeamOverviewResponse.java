package com.realtime_monitoring.query_service.dto;

import com.realtime_monitoring.query_service.model.TeamOverview;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeamOverviewResponse(
    UUID id,
    String name,
    String description,
    UUID tenantId,
    String tenantName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TeamOverviewResponse from(TeamOverview team) {
        return new TeamOverviewResponse(
            team.getId(),
            team.getName(),
            team.getDescription(),
            team.getTenantId(),
            team.getTenantName(),
            team.getCreatedAt(),
            team.getUpdatedAt()
        );
    }
}
