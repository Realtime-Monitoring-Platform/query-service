package com.realtime_monitoring.query_service.kafkaa;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.realtime_monitoring.query_service.event.TeamCreatedEvent;
import com.realtime_monitoring.query_service.event.TeamDeletedEvent;
import com.realtime_monitoring.query_service.event.TeamUpdatedEvent;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;
import com.realtime_monitoring.query_service.repository.TeamOverviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "team-events-v2", groupId = "${spring.kafka.consumer.group-id:query-service-group}")
public class TeamConsumer {

    private final TeamOverviewRepository teamOverviewRepository;
    private final TenantOverviewRepository tenantRepo;

    @KafkaHandler
    public void handleTeamCreated(@Payload TeamCreatedEvent event) {
        log.info("Received team creation event for team ID: {}", event.id());

        if (teamOverviewRepository.existsById(event.id())) {
            log.warn("TeamOverview {} already exists, skipping duplicate create event.", event.id());
            return;
        }

        TeamOverview teamOverview = new TeamOverview();
        teamOverview.setId(event.id());
        teamOverview.setDescription(event.description());
        teamOverview.setName(event.name());

        teamOverview.setTenantId(event.tenantId());

        String tenantName = tenantRepo.findById(event.tenantId())
                .map(TenantOverview::getName)
                .orElse(null);
        teamOverview.setTenantName(tenantName);
        this.tenantRepo.findById(event.tenantId()).ifPresent(tenant -> {
            tenant.setTeamNumber(tenant.getTeamNumber() + 1);
            tenantRepo.save(tenant);
        });

        if (tenantName == null) {
            log.warn("Tenant {} not found when processing team creation for team {}. TenantName left null.",
                    event.tenantId(), event.id());
        }

        log.info("Saving team overview for tenant ID: {}", event.tenantId());
        teamOverviewRepository.save(teamOverview);
    }

    @KafkaHandler
    public void handleTeamUpdated(@Payload TeamUpdatedEvent event) {
        log.info("Received team update event for team ID::::::::::::::::::: {}", event.id());

        TeamOverview teamOverview = teamOverviewRepository.findById(event.id()).orElse(null);
        if (teamOverview != null) {
            teamOverview.setId(event.id());
            teamOverview.setDescription(event.description());
            teamOverview.setName(event.name());
            teamOverview.setTenantId(event.tenantId());
            String tenantName = tenantRepo.findById(event.tenantId())
                    .map(TenantOverview::getName)
                    .orElse(null);
            teamOverview.setTenantName(tenantName);

            if (tenantName == null) {
                log.warn("Tenant {} not found when processing team update for team {}. TenantName left null.",
                        event.tenantId(), event.id());
            }

            log.info("Update team overview for tenant ID::::::::::::::::::: {}", event.id(), event.tenantId());
            teamOverviewRepository.save(teamOverview);
        } else {
            log.warn("Team with ID {} not found for update.", event.id());
        }
    }

    @KafkaHandler
    public void handleTeamDeleted(@Payload TeamDeletedEvent event) {
        log.info("Received team deletion event for team ID: {}::::::::::::::::::::::::::::::::::::::", event.id());
        
        TeamOverview teamOverview = teamOverviewRepository.findById(event.id()).orElse(null);
        if (teamOverview != null) {
            UUID tenantId = teamOverview.getTenantId();
            this.tenantRepo.findById(tenantId).ifPresent(tenant -> {
                tenant.setTeamNumber(tenant.getTeamNumber() );
                tenantRepo.save(tenant);
            });
        } else {
            log.warn("Team with ID {} not found for deletion.", event.id());
        }
       
        teamOverviewRepository.deleteById(event.id());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event: {}", event);
    }
}