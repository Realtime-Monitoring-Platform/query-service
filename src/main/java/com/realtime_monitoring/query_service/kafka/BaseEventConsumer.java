package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.BaseEvent;
import com.realtime_monitoring.query_service.service.ProjectionService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.Acknowledgment;
import org.springframework.kafka.listener.MessageListener;

public abstract class BaseEventConsumer<T> implements MessageListener<String, T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final ProjectionService projectionService;

    protected BaseEventConsumer(ProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    protected <E extends BaseEvent> void processEvent(ConsumerRecord<String, E> record, Acknowledgment ack, String eventType, java.util.function.Consumer<E> processor) {
        E event = record.value();
        if (event == null || event.event() == null) {
            log.warn("Received null event, skipping");
            ack.acknowledge();
            return;
        }

        String eventId = event.event().eventId().toString();
        String aggregateId = event.event().aggregateId().toString();

        log.info("Processing {} - EventId: {}, AggregateId: {}", eventType, eventId, aggregateId);

        try {
            if (projectionService.isEventProcessed(eventId)) {
                log.info("Event {} already processed, skipping", eventId);
                ack.acknowledge();
                return;
            }

            processor.accept(event);

            projectionService.markEventAsProcessed(eventId, eventType, aggregateId);
            ack.acknowledge();
            log.info("Successfully processed {} - EventId: {}", eventType, eventId);

        } catch (Exception e) {
            log.error("Failed to process {} - EventId: {}, Error: {}", eventType, eventId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void onMessage(ConsumerRecord<String, T> record, Acknowledgment ack) {
        processEvent(record, ack, getEventType(), this::handleEvent);
    }

    protected abstract void handleEvent(T event);

    protected abstract String getEventType();
}
