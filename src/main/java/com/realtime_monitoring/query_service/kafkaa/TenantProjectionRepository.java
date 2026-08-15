package com.realtime_monitoring.query_service.kafkaa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.model.UserOverview;

@Repository
public interface TenantProjectionRepository  extends JpaRepository<TenantOverview, UUID> {

   // List<UserOverview> findByTenantId(UUID tenantId);
    
}
