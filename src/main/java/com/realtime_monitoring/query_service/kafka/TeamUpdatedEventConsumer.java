package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.TeamUpdatedEvent;
import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.repository.TeamOverviewRepository;
import com.realtime_monitoring.query_service.service.ProjectionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class TeamUpdatedEventConsumer extends BaseEventConsumer<TeamUpdatedEvent> {

    private final TeamOverviewRepository teamOverviewRepository;

    public TeamUpdatedEventConsumer(ProjectionService projectionService, TeamOverviewRepository teamOverviewRepository) {
        super(projectionService);
        this.teamOverviewRepository = teamOverviewRepository;
    }

    @Override
    @KafkaListener(topics = EventTopics.TEAM_UPDATED, groupId = "query-service-group", concurrency = "3")
    public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String, TeamUpdatedEvent> record, Acknowledgment ack) {
        super.onMessage(record, ack);
    }

    @Override
    protected void handleEvent(TeamUpdatedEvent event) {
        TeamOverview overview = teamOverviewRepository.findById(event.event().aggregateId())
                .orElse(new TeamOverview());
        overview.setId(event.event().aggregateId());
        overview.setName(event.name());
        overview.setDescription(event.description());
        overview.setTenantId(event.tenantId());
        teamOverviewRepository.save(overview);
    }

    @Override
    protected String getEventType() {
        return "TEAM_UPDATED";
    }
}
