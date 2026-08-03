package com.realtime_monitoring.query_service.kafkaa;

import java.util.Set;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.realtime_monitoring.query_service.event.RoleCreatedEvent;
import com.realtime_monitoring.query_service.event.RoleDeletedEvent;
import com.realtime_monitoring.query_service.event.RoleUpdatedEvent;
import com.realtime_monitoring.query_service.event.TeamCreatedEvent;
import com.realtime_monitoring.query_service.event.TeamDeletedEvent;
import com.realtime_monitoring.query_service.event.TeamUpdatedEvent;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.model.PermissionOverview;
import com.realtime_monitoring.query_service.model.RoleOverview;
import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;
import com.realtime_monitoring.query_service.repository.RoleOverviewRepository;
import com.realtime_monitoring.query_service.repository.TeamOverviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "role-events-v1", groupId = "${spring.kafka.consumer.group-id:query-service-group}")
public class RoleConsumer {

    private final RoleOverviewRepository roleOverviewRepository;
    private final TenantOverviewRepository tenantRepo;

    @KafkaHandler
    public void handleRoleCreated(@Payload RoleCreatedEvent event) {
        log.info("Received role creation event for role ID: {}", event.roleId());
        log.info(
                "Received role creation event for role name: {} /////////////////////////////////////////////////////////////////////////////////////////",
                event.name());
        if (roleOverviewRepository.existsById(event.roleId())) {
            log.warn("RoleOverview {} already exists, skipping duplicate create event.", event.roleId());
            return;
        }

        RoleOverview roleOverview = new RoleOverview();
        roleOverview.setId(event.roleId());
        roleOverview.setDescription(event.description());
        roleOverview.setName(event.name());
        Set<PermissionOverview> permissionIds = event.permissionIds().stream()
                .map(permissionId -> {
                    PermissionOverview permissionOverview = new PermissionOverview();
                    permissionOverview.setId(permissionId);
                    return permissionOverview;
                })
                .collect(java.util.stream.Collectors.toSet());
        roleOverview.setPermissions(permissionIds);
        // roleOverview.setTenantId(event.tenantId());

        // roleOverview.setTenantName(tenantName);

        log.info("Saving role overview for tenant ID: {}", event.roleId());
        roleOverviewRepository.save(roleOverview);
    }

    @KafkaHandler
    public void handleRoleUpdated(@Payload RoleUpdatedEvent event) {
        log.info("Received role update event for role ID::::::::::::::::::: {}", event.roleId());

        RoleOverview roleOverview = roleOverviewRepository.findById(event.roleId()).orElse(null);
        if (roleOverview != null) {
            roleOverview.setId(event.roleId());
            roleOverview.setDescription(event.description());
            roleOverview.setName(event.name());
            Set<PermissionOverview> permissionIds = event.permissionIds().stream()
                    .map(permissionId -> {
                        PermissionOverview permissionOverview = new PermissionOverview();
                        permissionOverview.setId(permissionId);
                        return permissionOverview;
                    })
                    .collect(java.util.stream.Collectors.toSet());
            roleOverview.setPermissions(permissionIds);
            log.info("Update role overview for tenant ID::::::::::::::::::: {}", event.roleId());
            roleOverviewRepository.save(roleOverview);
        } else {
            log.warn("Role with ID {} not found for update.", event.roleId());
        }
    }

    @KafkaHandler
    public void handleTeamDeleted(@Payload RoleDeletedEvent event) {
        log.info("Received team deletion event for team ID: {}::::::::::::::::::::::::::::::::::::::", event.id());
        this.roleOverviewRepository.deleteById(event.id());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event: {}", event);
    }
}