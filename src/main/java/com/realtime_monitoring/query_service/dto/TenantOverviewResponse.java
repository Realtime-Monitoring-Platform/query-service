package com.realtime_monitoring.query_service.dto;

import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.model.TenantStatusOverview;

import java.time.LocalDateTime;
import java.util.UUID;

public record TenantOverviewResponse(
    UUID id,
    String name,
    String phone,
    String companyName,
    TenantStatusOverview status,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TenantOverviewResponse from(TenantOverview tenant) {
        return new TenantOverviewResponse(
            tenant.getId(),
            tenant.getName(),
            tenant.getPhone(),
            tenant.getCompanyName(),
            tenant.getStatus(),
            tenant.getEmail(),
            tenant.getCreatedAt(),
            tenant.getUpdatedAt()
        );
    }
}
