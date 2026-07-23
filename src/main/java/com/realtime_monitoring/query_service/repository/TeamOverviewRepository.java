package com.realtime_monitoring.query_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realtime_monitoring.query_service.model.TeamOverview;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamOverviewRepository extends JpaRepository<TeamOverview, UUID> {
    List<TeamOverview> findByTenantId(UUID tenantId);
}
