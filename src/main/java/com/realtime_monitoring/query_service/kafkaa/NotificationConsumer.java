package com.realtime_monitoring.query_service.kafkaa;

import com.realtime_monitoring.query_service.event.NotificationCreatedEvent;
import com.realtime_monitoring.query_service.model.NotificationOverview;
import com.realtime_monitoring.query_service.repository.NotificationOverviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@KafkaListener(
        topics = "notification-events-v1",
        groupId = "${spring.kafka.consumer.group-id:query-service-group}",
        containerFactory = "kafkaListenerContainerFactory")
public class NotificationConsumer {


    private final NotificationOverviewRepository notificationOverviewRepository;

    @KafkaHandler
    public void handleNotificationCreated(@Payload NotificationCreatedEvent event) {
        NotificationOverview notificationOverview = notificationOverviewRepository
                .findById(event.id())
                .orElseGet(NotificationOverview::new);

        notificationOverview.setId(event.id());

        notificationOverview.setTitle(event.title());
        notificationOverview.setDescription(event.description());
        notificationOverview.setType(event.type());
        
        notificationOverview.setUserId(event.userId());
        notificationOverview.setTenantId(event.tenantId());
        notificationOverview.setCreatedAt(event.createdAt());
        notificationOverview.setRead(event.read());

        notificationOverviewRepository.save(notificationOverview);
        log.info("Saved notification overview with id={}", event.id());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object event) {
        log.warn("Unknown notification event: {}", event);
    }
}
