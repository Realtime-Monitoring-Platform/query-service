package com.realtime_monitoring.query_service.repository;

import com.realtime_monitoring.query_service.model.TeamOverview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamOverviewRepository extends JpaRepository<TeamOverview, UUID> {
    Page<TeamOverview> findByTenantId(UUID tenantId, Pageable pageable);
}