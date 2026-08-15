package com.realtime_monitoring.query_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realtime_monitoring.query_service.model.RoleOverview;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleOverviewRepository extends JpaRepository<RoleOverview, UUID> {
    Optional<RoleOverview> findByName(String name);
    boolean existsByName(String name);
}
