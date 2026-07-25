package com.realtime_monitoring.query_service.kafkaa;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.realtime_monitoring.query_service.event.DeviceCreatedEvent;
import com.realtime_monitoring.query_service.event.DeviceDeletedEvent;
import com.realtime_monitoring.query_service.event.DeviceUpdatedEvent;
import com.realtime_monitoring.query_service.event.TenantDeletedEvent;
import com.realtime_monitoring.query_service.event.UserCreatedEvent;
import com.realtime_monitoring.query_service.event.UserDeletedEvent;
import com.realtime_monitoring.query_service.event.UserUpdatedEvent;
import com.realtime_monitoring.query_service.model.DeviceOverview;
import com.realtime_monitoring.query_service.model.DeviceStatusOverview;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.model.UserOverview;
import com.realtime_monitoring.query_service.model.UserStatusOverview;
import com.realtime_monitoring.query_service.repository.DeviceOverviewRepository;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;
import com.realtime_monitoring.query_service.repository.UserOverviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "user-events-v5", groupId = "${spring.kafka.consumer.group-id:query-service-group}")
public class UserConsumer {

    private final UserOverviewRepository userOverviewRepository;
    private final TenantOverviewRepository tenantRepo;

    @KafkaHandler
public void handleUserCreated(@Payload UserCreatedEvent event) {
    log.info("Received user creation event for user ID: {}", event.id());

    if (userOverviewRepository.existsById(event.id())) {
        log.warn("UserOverview {} already exists, skipping duplicate create event.", event.id());
        return;
    }

    UserOverview userOverview = new UserOverview();
    userOverview.setId(event.id());
    userOverview.setUsername(event.username());
    userOverview.setEmail(event.email());
    userOverview.setFirstName(event.firstName());
    userOverview.setLastName(event.lastName());
    userOverview.setPhone(event.phone());
    userOverview.setAvatarUrl(event.avatarUrl());
    userOverview.setAddress(event.Address());
    userOverview.setStatus(event.status() != null ? UserStatusOverview.valueOf(event.status()) : null);
    userOverview.setTenantId(event.tenantId());

    String tenantName = tenantRepo.findById(event.tenantId())
            .map(TenantOverview::getName)
            .orElse(null);
    userOverview.setTenantName(tenantName);

    if (tenantName == null) {
        log.warn("Tenant {} not found when processing user creation for user {}. TenantName left null.",
                event.tenantId(), event.id());
    }

    log.info("Saving user overview for tenant ID: {}", event.tenantId());
    userOverviewRepository.save(userOverview);
}

    @KafkaHandler
    public void handleUserUpdated(@Payload UserUpdatedEvent event) {
        log.info("Received user update event for user ID::::::::::::::::::: {}", event.id());

        UserOverview userOverview = userOverviewRepository.findById(event.id()).orElse(null);
        if (userOverview != null) {
            userOverview.setUsername(event.username());
            userOverview.setEmail(event.email());
            userOverview.setFirstName(event.firstName());
            userOverview.setLastName(event.lastName());
            userOverview.setPhone(event.phone());
            userOverview.setAvatarUrl(event.avatarUrl());
            userOverview.setAddress(event.Address());
            userOverview.setStatus(event.status() != null ? UserStatusOverview.valueOf(event.status()) : null);
            userOverview.setTenantId(event.tenantId());
            String tenantName = tenantRepo.findById(event.tenantId())
                    .map(TenantOverview::getName)
                    .orElse(null);
            userOverview.setTenantName(tenantName);

            if (tenantName == null) {
                log.warn("Tenant {} not found when processing user update for user {}. TenantName left null.",
                        event.tenantId(), event.id());
            }

            log.info("Update user overview for tenant ID::::::::::::::::::: {}", event.teamId(), event.tenantId());
            userOverviewRepository.save(userOverview);
        } else {
            log.warn("User with ID {} not found for update.", event.id());
        }
    }

    @KafkaHandler
    public void handleUserDeleted(@Payload UserDeletedEvent event) {
        log.info("Received user deletion event for user ID: {}::::::::::::::::::::::::::::::::::::::", event.id());
        userOverviewRepository.deleteById(event.id());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event: {}", event);
    }
}