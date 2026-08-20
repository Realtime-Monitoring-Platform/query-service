package com.realtime_monitoring.query_service.repository;

import com.realtime_monitoring.query_service.model.NotificationOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationOverviewRepository extends JpaRepository<NotificationOverview, String> {


    
    List<NotificationOverview> findByUserIdOrderByCreatedAtDesc(String userId);

    List<NotificationOverview> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<NotificationOverview> findByUserIdAndReadFalseOrderByCreatedAtDesc(String userId);

    long countByUserIdAndReadFalse(String userId);
}
