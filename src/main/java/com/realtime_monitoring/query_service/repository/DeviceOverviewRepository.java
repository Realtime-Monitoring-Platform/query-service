package com.realtime_monitoring.query_service.repository;

import com.realtime_monitoring.query_service.model.DeviceOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface DeviceOverviewRepository extends JpaRepository<DeviceOverview, UUID> {
    List<DeviceOverview> findByTenantId(UUID tenantId);
    List<DeviceOverview> findByTeamId(UUID teamId);
    Optional<DeviceOverview> findByHostname(String hostname);
}
