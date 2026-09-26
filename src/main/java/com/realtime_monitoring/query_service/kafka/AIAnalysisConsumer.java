package com.realtime_monitoring.query_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtime_monitoring.query_service.model.AIAnalysis;
import com.realtime_monitoring.query_service.event.AIAnalysisResultEvent;
import com.realtime_monitoring.query_service.repository.AIAnalysisRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AIAnalysisConsumer {

    private final AIAnalysisRepository repository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ai-analysis-results", groupId = "query-service-ai-analysis", containerFactory = "aiAnalysisKafkaListenerContainerFactory")
    public void consume(AIAnalysisResultEvent event) {

        try {

                List<Map<String, Object>> recommendations = event.recommendations().stream()
                    .map(recommendation -> {
                    Map<String, Object> value = new LinkedHashMap<>();
                    value.put("priority", recommendation.priority());
                    value.put("action", recommendation.action());
                    value.put("command", recommendation.command());
                    value.put("execution_status", recommendation.executionStatus());
                    value.put("execution_output", recommendation.executionOutput());
                    value.put("execution_error", recommendation.executionError());
                    return value;
                    })
                    .toList();

                String recommendationsJson = objectMapper.writeValueAsString(recommendations);
                
            AIAnalysis analysis = AIAnalysis.builder()
                    .incidentId(event.incidentId())
                    .deviceId(event.deviceId())
                    .tenantId(event.tenantId())
                    .severity(event.severity())
                    .problem(event.problem())
                    .rootCause(event.rootCause())
                    .confidence(event.confidence())
                    .impact(event.impact())
                    .recommendations(recommendationsJson)
                    .createdAt(Instant.now())
                    .build();

            repository.save(analysis);

            System.out.println(
                    "AI analysis saved: " + event.incidentId());

        } catch (Exception e) {
            System.err.println(
                    "Failed to save AI analysis: " + e.getMessage());
        }
    }
}