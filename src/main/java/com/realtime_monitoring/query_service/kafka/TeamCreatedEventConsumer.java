package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.TeamCreatedEvent;
import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.repository.TeamOverviewRepository;
import com.realtime_monitoring.query_service.service.ProjectionService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class TeamCreatedEventConsumer extends BaseEventConsumer<TeamCreatedEvent> {

    private final TeamOverviewRepository teamOverviewRepository;

    public TeamCreatedEventConsumer(ProjectionService projectionService, TeamOverviewRepository teamOverviewRepository) {
        super(projectionService);
        this.teamOverviewRepository = teamOverviewRepository;
    }

    @Override
    @KafkaListener(topics = EventTopics.TEAM_CREATED, groupId = "query-service-group", concurrency = "3")
    public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String, TeamCreatedEvent> record, Acknowledgment ack) {
        super.onMessage(record, ack);
    }

    @Override
    protected void handleEvent(TeamCreatedEvent event) {
        TeamOverview overview = new TeamOverview();
        overview.setId(event.event().aggregateId());
        overview.setName(event.name());
        overview.setDescription(event.description());
        overview.setTenantId(event.tenantId());
        teamOverviewRepository.save(overview);
    }

    @Override
    protected String getEventType() {
        return "TEAM_CREATED";
    }

    @Override
    public void onMessage(ConsumerRecord<String, TeamCreatedEvent> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }
}
