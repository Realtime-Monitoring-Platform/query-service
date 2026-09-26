package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.ProblemVerificationEvent;
import com.realtime_monitoring.query_service.repository.AIAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProblemVerificationConsumer {

    
    private final AIAnalysisRepository repository;

    @KafkaListener(
            topics = "ai-verification-results",
            groupId = "query-service-ai-verification",
            containerFactory = "problemVerificationKafkaListenerContainerFactory")
    public void consume(ProblemVerificationEvent event) {
        repository.findByIncidentId(event.incidentId()).ifPresent(analysis -> {
            analysis.setProblemStatus(event.problemStatus());
            analysis.setVerificationConfidence(event.confidence());
            analysis.setVerificationExplanation(event.explanation());
            analysis.setVerificationNextAction(event.nextAction());
            repository.save(analysis);
        });
    }
}
