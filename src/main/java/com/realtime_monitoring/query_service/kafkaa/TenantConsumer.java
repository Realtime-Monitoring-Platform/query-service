package com.realtime_monitoring.query_service.kafkaa;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.realtime_monitoring.query_service.event.TenantCreatedEvent;
import com.realtime_monitoring.query_service.event.TenantUpdatedEvent;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(
    topics = "tenant-events-v4",
    groupId = "${spring.kafka.consumer.group-id:query-service-group}"
)
public class TenantConsumer {

    private final TenantOverviewRepository tenantOverviewRepository;

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

        tenantOverviewRepository.save(tenantOverview);
    }

    @KafkaHandler
    public void handleTenantUpdated(@Payload TenantUpdatedEvent event) {
        log.info("Received tenant update event for tenant ID: {} :::::::::::::::::::::::::::::::::::::::::::::", event.id());
    }

    @KafkaHandler
    public void handleTenantDeleted(@Payload UUID tenantId) {
        log.info("Received tenant deletion event for tenant ID: {}::::::::::::::::::::::::::::::::::::::", tenantId);
        tenantOverviewRepository.deleteById(tenantId);
    }


    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event: {}", event);
    }
}