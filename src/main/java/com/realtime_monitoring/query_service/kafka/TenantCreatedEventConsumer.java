package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.TenantCreatedEvent;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.model.TenantStatusOverview;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;
import com.realtime_monitoring.query_service.service.ProjectionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class TenantCreatedEventConsumer extends BaseEventConsumer<TenantCreatedEvent> {

    private final TenantOverviewRepository tenantOverviewRepository;

    public TenantCreatedEventConsumer(ProjectionService projectionService, TenantOverviewRepository tenantOverviewRepository) {
        super(projectionService);
        this.tenantOverviewRepository = tenantOverviewRepository;
    }

    @Override
    @KafkaListener(topics = EventTopics.TENANT_CREATED, groupId = "query-service-group", concurrency = "3")
    public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String, TenantCreatedEvent> record, Acknowledgment ack) {
        super.onMessage(record, ack);
    }

    @Override
    protected void handleEvent(TenantCreatedEvent event) {
        TenantOverview overview = new TenantOverview();
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
        return "TENANT_CREATED";
    }
}
