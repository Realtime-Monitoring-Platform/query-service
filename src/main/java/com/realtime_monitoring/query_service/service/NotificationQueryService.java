package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.model.NotificationOverview;
import com.realtime_monitoring.query_service.repository.NotificationOverviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {

    private final NotificationOverviewRepository notificationOverviewRepository;

    public Optional<NotificationOverview> getById(String id) {
        return notificationOverviewRepository.findById(id);
    }




    public List<NotificationOverview> getByUserId(String userId) {
        return notificationOverviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    

    public List<NotificationOverview> getByTenantId(String tenantId) {
        return notificationOverviewRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    public List<NotificationOverview> getUnreadByUserId(String userId) {
        return notificationOverviewRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }


    public long getUnreadCountByUserId(String userId) {
        return notificationOverviewRepository.countByUserIdAndReadFalse(userId);
    }
}
