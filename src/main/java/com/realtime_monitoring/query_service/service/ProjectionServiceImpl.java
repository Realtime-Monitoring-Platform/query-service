package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.model.ProcessedEvent;
import com.realtime_monitoring.query_service.repository.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProjectionServiceImpl implements ProjectionService {

    private static final Logger log = LoggerFactory.getLogger(ProjectionServiceImpl.class);
    private final ProcessedEventRepository processedEventRepository;

    public ProjectionServiceImpl(ProcessedEventRepository processedEventRepository) {
        this.processedEventRepository = processedEventRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEventProcessed(UUID eventId) {
        return processedEventRepository.existsById(eventId);
    }

    @Override
    @Transactional
    public void markEventAsProcessed(UUID eventId, String eventType, String aggregateId) {
        if (!isEventProcessed(eventId)) {
            ProcessedEvent processedEvent = new ProcessedEvent();
            processedEvent.setId(eventId);
            processedEvent.setEventType(eventType);
            processedEvent.setAggregateId(UUID.fromString(aggregateId));
            processedEventRepository.save(processedEvent);
            log.debug("Marked event {} as processed", eventId);
        }
    }
}
