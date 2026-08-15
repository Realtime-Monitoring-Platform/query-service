package com.realtime_monitoring.query_service.dto;

import com.realtime_monitoring.query_service.model.UserOverview;
import com.realtime_monitoring.query_service.model.UserStatusOverview;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserOverviewResponse(
    UUID id,
    String username,
    String email,
    String firstName,
    String lastName,
    String phone,
    String avatarUrl,
    String address,
    UserStatusOverview status,
    LocalDateTime lastLogin,
    
    UUID tenantId,
    String tenantName,
    UUID teamId,
    String teamName,
    String roleName,
    UUID roleId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserOverviewResponse from(UserOverview user) {
        return new UserOverviewResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhone(),
            user.getAvatarUrl(),
            user.getAddress(),
            user.getStatus(),
            user.getLastLogin(),
            user.getTenantId(),
            user.getTenantName(),
            user.getTeamId(),
            user.getTeamName(),
            user.getRoleName(),
            user.getRoleId(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
