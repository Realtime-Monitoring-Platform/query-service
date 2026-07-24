package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.TenantUpdatedEvent;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.model.TenantStatusOverview;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;
import com.realtime_monitoring.query_service.service.ProjectionService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class TenantUpdatedEventConsumer extends BaseEventConsumer<TenantUpdatedEvent> {

    private final TenantOverviewRepository tenantOverviewRepository;

    public TenantUpdatedEventConsumer(ProjectionService projectionService, TenantOverviewRepository tenantOverviewRepository) {
        super(projectionService);
        this.tenantOverviewRepository = tenantOverviewRepository;
    }

    @Override
    @KafkaListener(topics = EventTopics.TENANT_UPDATED, groupId = "query-service-group", concurrency = "3")
    public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String, TenantUpdatedEvent> record, Acknowledgment ack) {
        super.onMessage(record, ack);
    }

    @Override
    protected void handleEvent(TenantUpdatedEvent event) {
        TenantOverview overview = tenantOverviewRepository.findById(event.event().aggregateId())
                .orElse(new TenantOverview());
        overview.setId(event.event().aggregateId());
        overview.setName(event.name());
        overview.setCompanyName(event.companyName());
        overview.setEmail(event.email());
        overview.setPhone(event.phone());
        overview.setStatus(TenantStatusOverview.valueOf(event.status()));
        tenantOverviewRepository.save(overview);
    }

    @Override
    protected String getEventType() {
        return "TENANT_UPDATED";
    }

    @Override
    public void onMessage(ConsumerRecord<String, TenantUpdatedEvent> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }
}
