package com.realtime_monitoring.query_service.repository;

import com.realtime_monitoring.query_service.model.PermissionOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionOverviewRepository extends JpaRepository<PermissionOverview, UUID> {
    Optional<PermissionOverview> findByName(String name);
}
