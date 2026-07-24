package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.DeviceCreatedEvent;
import com.realtime_monitoring.query_service.model.DeviceOverview;
import com.realtime_monitoring.query_service.model.DeviceStatusOverview;
import com.realtime_monitoring.query_service.repository.DeviceOverviewRepository;
import com.realtime_monitoring.query_service.service.ProjectionService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class DeviceCreatedEventConsumer extends BaseEventConsumer<DeviceCreatedEvent> {

    private final DeviceOverviewRepository deviceOverviewRepository;

    public DeviceCreatedEventConsumer(ProjectionService projectionService, DeviceOverviewRepository deviceOverviewRepository) {
        super(projectionService);
        this.deviceOverviewRepository = deviceOverviewRepository;
    }

    @Override
    @KafkaListener(topics = EventTopics.DEVICE_CREATED, groupId = "query-service-group", concurrency = "3")
    public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String, DeviceCreatedEvent> record, Acknowledgment ack) {
        super.onMessage(record, ack);
    }

    @Override
    protected void handleEvent(DeviceCreatedEvent event) {
        DeviceOverview overview = new DeviceOverview();
        overview.setId(event.event().aggregateId());
        overview.setTenantId(event.tenantId());
        overview.setTeamId(event.teamId());
        overview.setAssignedUserId(event.assignedUserId());
        overview.setDeviceName(event.deviceName());
        overview.setModel(event.model());
        overview.setManufacturer(event.manufacturer());
        overview.setHostname(event.hostname());
        overview.setIpAddress(event.ipAddress());
        overview.setMacAddress(event.macAddress());
        overview.setLocation(event.location());
        overview.setStatus(DeviceStatusOverview.valueOf(event.status()));
        deviceOverviewRepository.save(overview);
    }

    @Override
    protected String getEventType() {
        return "DEVICE_CREATED";
    }

    @Override
    public void onMessage(ConsumerRecord<String, DeviceCreatedEvent> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }
}
