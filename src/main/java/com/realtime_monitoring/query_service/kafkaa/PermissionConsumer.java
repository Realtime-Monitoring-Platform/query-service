package com.realtime_monitoring.query_service.kafkaa;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.realtime_monitoring.query_service.event.PermissionCreatedEvent;
import com.realtime_monitoring.query_service.event.PermissionDeletedEvent;
import com.realtime_monitoring.query_service.event.PermissionUpdatedEvent;
import com.realtime_monitoring.query_service.model.PermissionOverview;
import com.realtime_monitoring.query_service.repository.PermissionOverviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "permission-events-v1", groupId = "${spring.kafka.consumer.group-id:query-service-group}")
public class PermissionConsumer {

    private final PermissionOverviewRepository permissionOverviewRepository;

    @KafkaHandler
    public void handlePermissionCreated(@Payload PermissionCreatedEvent event) {
        log.info("Received permission creation event for permission ID: {}", event.permissionId());

        if (permissionOverviewRepository.existsById(event.permissionId())) {
            log.warn("PermissionOverview {} already exists, skipping duplicate create event.", event.permissionId());
            return;
        }

        PermissionOverview permissionOverview = new PermissionOverview();
        permissionOverview.setId(event.permissionId());
        permissionOverview.setName(event.name());
        permissionOverview.setDescription(event.description());
        permissionOverview.setResource(event.resource());
        permissionOverview.setAction(event.action());

        log.info("Saving permission overview for permission ID: {}", event.permissionId());
        permissionOverviewRepository.save(permissionOverview);
    }

    @KafkaHandler
    public void handlePermissionUpdated(@Payload PermissionUpdatedEvent event) {
        log.info("Received permission update event for permission ID::::::::::::::::::: {}", event.permissionId());

        PermissionOverview permissionOverview = permissionOverviewRepository.findById(event.permissionId()).orElse(null);
        if (permissionOverview != null) {
            permissionOverview.setName(event.name());
            permissionOverview.setDescription(event.description());
            permissionOverview.setResource(event.resource());
            permissionOverview.setAction(event.action());

            log.info("Update permission overview for permission ID::::::::::::::::::: {}", event.permissionId());
            permissionOverviewRepository.save(permissionOverview);
        } else {
            log.warn("Permission with ID {} not found for update.", event.permissionId());
        }
    }

    @KafkaHandler
    public void handlePermissionDeleted(@Payload PermissionDeletedEvent event) {
        log.info("Received permission deletion event for permission ID: {}::::::::::::::::::::::::::::::::::::::", event.permissionId());
        this.permissionOverviewRepository.deleteById(event.permissionId());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event: {}", event);
    }
}