package com.realtime_monitoring.query_service.repository;

import com.realtime_monitoring.query_service.model.AIAnalysis;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AIAnalysisRepository extends JpaRepository<AIAnalysis, UUID> {

        Optional<AIAnalysis> findByIncidentId(UUID incidentId);

        Page<AIAnalysis> findByDeviceIdOrderByCreatedAtDesc(UUID deviceId, Pageable pageable);

        Page<AIAnalysis> findByTenantIdOrderByCreatedAtDesc(UUID tenantId, Pageable pageable);
}
