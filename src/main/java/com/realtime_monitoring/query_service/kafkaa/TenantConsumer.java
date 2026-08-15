package com.realtime_monitoring.query_service.kafkaa;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.realtime_monitoring.query_service.event.TenantCreatedEvent;
import com.realtime_monitoring.query_service.event.TenantDeletedEvent;
import com.realtime_monitoring.query_service.event.TenantUpdatedEvent;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;
import com.realtime_monitoring.query_service.repository.UserOverviewRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(
    topics = "tenant-events-v6",
    groupId = "${spring.kafka.consumer.group-id:query-service-group}"
)
public class TenantConsumer {

    private final TenantOverviewRepository tenantOverviewRepository;
    private final UserOverviewRepository userOverviewRepository;
    @KafkaHandler
    public void handleTenantCreated(@Payload TenantCreatedEvent event) {

        log.info("Received tenant creation event for tenant ID::::::::::::::::: {}", event.id());

        TenantOverview tenantOverview = new TenantOverview();
        tenantOverview.setId(event.id());
        tenantOverview.setName(event.name());
        tenantOverview.setCompanyName(event.companyName());
        tenantOverview.setEmail(event.email());
        tenantOverview.setPhone(event.phone());
        tenantOverview.setStatus(event.status());
        tenantOverview.setAdminId(event.AdminId());
        tenantOverview.setAdminName(this.userOverviewRepository.findById(event.AdminId()).map(user -> user.getFirstName() + " " + user.getLastName()).orElse(null));
          
        tenantOverviewRepository.save(tenantOverview);
    }

    @KafkaHandler
    public void handleTenantUpdated(@Payload TenantUpdatedEvent event) {
        log.info("Received tenant update event for tenant ID: {} :::::::::::::::::::::::::::::::::::::::::::::", event.id());
        TenantOverview tenantOverview = tenantOverviewRepository.findById(event.id()).orElse(null);
        if (tenantOverview != null) {
            tenantOverview.setName(event.name());
            tenantOverview.setCompanyName(event.companyName());
            tenantOverview.setEmail(event.email());
            tenantOverview.setPhone(event.phone());
            tenantOverview.setStatus(event.status());
            tenantOverview.setAdminId(event.AdminId());
            tenantOverview.setAdminName(this.userOverviewRepository.findById(event.AdminId()).map(user -> user.getFirstName() + " " + user.getLastName()).orElse(null));
            tenantOverviewRepository.save(tenantOverview);
        } else {
            log.warn("Tenant with ID {} not found for update.", event.id());
        }
    }

    @KafkaHandler
    public void handleTenantDeleted(@Payload TenantDeletedEvent event) {
        log.info("Received tenant deletion event for tenant ID: {}::::::::::::::::::::::::::::::::::::::", event.id());
        tenantOverviewRepository.deleteById(event.id());
    }


    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event: {}", event);
    }
}