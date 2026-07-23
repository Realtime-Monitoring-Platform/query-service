package com.realtime_monitoring.query_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realtime_monitoring.query_service.model.UserOverview;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface UserOverviewRepository extends JpaRepository<UserOverview, UUID> {
    Optional<UserOverview> findByEmail(String email);
    Optional<UserOverview> findByUsername(String username);
    List<UserOverview> findByTenantId(UUID tenantId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
