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
import com.realtime_monitoring.query_service.model.DeviceOverview;
import com.realtime_monitoring.query_service.model.DeviceStatusOverview;
import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.repository.DeviceOverviewRepository;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "device-events-v5", groupId = "${spring.kafka.consumer.group-id:query-service-group}")
public class DeviceConsumer {

    private final DeviceOverviewRepository deviceOverviewRepository;
    private final TenantOverviewRepository tenantRepo;

    @KafkaHandler
    public void handleDeviceCreated(@Payload DeviceCreatedEvent event) {
        log.info("Received device creation event for device ID::::::::::::::::::: {}", event.id());

        DeviceOverview deviceOverview = new DeviceOverview();
        deviceOverview.setId(event.id());
        deviceOverview.setAssignedUserId(event.assignedUserId());
        deviceOverview.setDeviceName(event.deviceName());
        deviceOverview.setModel(event.model());
        deviceOverview.setManufacturer(event.manufacturer());
        deviceOverview.setHostname(event.hostname());
        deviceOverview.setIpAddress(event.ipAddress());
        deviceOverview.setMacAddress(event.macAddress());
        deviceOverview.setLocation(event.location());
        deviceOverview.setStatus(event.status() != null ? DeviceStatusOverview.valueOf(event.status()) : null);
        deviceOverview.setLastSeen(event.lastSeen());
        deviceOverview.setTenantId(event.tenantId());
        deviceOverview.setTeamId(event.teamId());

        deviceOverview.setTenantName(this.tenantRepo.findById(event.tenantId()).orElse(null).getName());
        log.info("Saving device overview for tenant ID::::::::::::::::::: {}", event.teamId(), event.tenantId());
        deviceOverviewRepository.save(deviceOverview);
    }

    @KafkaHandler
    public void handleDeviceUpdated(@Payload DeviceUpdatedEvent event) {
        log.info("Received device update event for device ID::::::::::::::::::: {}", event.id());

        DeviceOverview deviceOverview = deviceOverviewRepository.findById(event.id()).orElse(null);
        if (deviceOverview != null) {
            deviceOverview.setAssignedUserId(event.assignedUserId());
            deviceOverview.setDeviceName(event.deviceName());
            deviceOverview.setModel(event.model());
            deviceOverview.setManufacturer(event.manufacturer());
            deviceOverview.setHostname(event.hostname());
            deviceOverview.setIpAddress(event.ipAddress());
            deviceOverview.setMacAddress(event.macAddress());
            deviceOverview.setLocation(event.location());
            deviceOverview.setStatus(event.status() != null ? DeviceStatusOverview.valueOf(event.status()) : null);
            deviceOverview.setLastSeen(event.lastSeen());
            deviceOverview.setTeamId(event.teamId());
            deviceOverview.setTenantId(event.tenantId());
            deviceOverview.setTenantName(this.tenantRepo.findById(event.tenantId()).orElse(null).getName());

            deviceOverviewRepository.save(deviceOverview);

            this.tenantRepo.findById(event.tenantId()).ifPresent(tenant -> {
                tenant.setTeamNumber(tenant.getTeamNumber());
                tenantRepo.save(tenant);
            });

        } else {
            log.warn("Device with ID {} not found for update.", event.id());
        }
    }

    @KafkaHandler
    public void handleDeviceDeleted(@Payload DeviceDeletedEvent event) {
        log.info("Received device deletion event for device ID: {}::::::::::::::::::::::::::::::::::::::", event.id());
        DeviceOverview deviceOverview = deviceOverviewRepository.findById(event.id()).orElse(null);
        if (deviceOverview != null) {
            UUID tenantId = deviceOverview.getTenantId();
            this.tenantRepo.findById(tenantId).ifPresent(tenant -> {
                tenant.setTeamNumber(tenant.getTeamNumber());
                tenantRepo.save(tenant);
            });
        } else {
            log.warn("Device with ID {} not found for deletion.", event.id());
        }
        deviceOverviewRepository.deleteById(event.id());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown event: {}", event);
    }
}