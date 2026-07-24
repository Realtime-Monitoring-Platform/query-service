package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.UserCreatedEvent;
import com.realtime_monitoring.query_service.model.UserOverview;
import com.realtime_monitoring.query_service.model.UserStatusOverview;
import com.realtime_monitoring.query_service.repository.UserOverviewRepository;
import com.realtime_monitoring.query_service.service.ProjectionService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventConsumer extends BaseEventConsumer<UserCreatedEvent> {

    private final UserOverviewRepository userOverviewRepository;

    public UserCreatedEventConsumer(ProjectionService projectionService, UserOverviewRepository userOverviewRepository) {
        super(projectionService);
        this.userOverviewRepository = userOverviewRepository;
    }

    @Override
    @KafkaListener(topics = EventTopics.USER_CREATED, groupId = "query-service-group", concurrency = "3")
    public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String, UserCreatedEvent> record, Acknowledgment ack) {
        super.onMessage(record, ack);
    }

    @Override
    protected void handleEvent(UserCreatedEvent event) {
        UserOverview overview = new UserOverview();
        overview.setId(event.event().aggregateId());
        overview.setUsername(event.username());
        overview.setEmail(event.email());
        overview.setFirstName(event.firstName());
        overview.setLastName(event.lastName());
        overview.setPhone(event.phone());
        overview.setAvatarUrl(event.avatarUrl());
        overview.setAddress(event.address());
        overview.setTenantId(event.tenantId());
        overview.setTeamId(event.teamId());
        overview.setStatus(UserStatusOverview.valueOf(event.status()));
        userOverviewRepository.save(overview);
    }

    @Override
    protected String getEventType() {
        return "USER_CREATED";
    }

    @Override
    public void onMessage(ConsumerRecord<String, UserCreatedEvent> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }
    
}
