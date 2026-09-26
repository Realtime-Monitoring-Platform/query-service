package com.realtime_monitoring.query_service.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtime_monitoring.query_service.dto.AIAnalysisResponse;
import com.realtime_monitoring.query_service.dto.AIRecommendationStatusRequest;
import com.realtime_monitoring.query_service.model.AIAnalysis;
import com.realtime_monitoring.query_service.repository.AIAnalysisRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/ai-analyses")
@RequiredArgsConstructor
public class AIAnalysisController {

    private final AIAnalysisRepository repository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<AIAnalysisResponse> getAllAnalyses() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/device/{deviceId}")
    public Page<AIAnalysisResponse> getByDevice(
            @PathVariable UUID deviceId,
            Pageable pageable) {

        return repository
                .findByDeviceIdOrderByCreatedAtDesc(deviceId, pageable)
                .map(this::toResponse);
    }

    @GetMapping("/tenant/{tenantId}")
    public Page<AIAnalysisResponse> getByTenant(
            @PathVariable UUID tenantId,
            Pageable pageable) {

        return repository.findByTenantIdOrderByCreatedAtDesc(tenantId, pageable)
                .map(this::toResponse);
    }

    @PatchMapping("/{incidentId}/recommendations/{priority}/status")
    public AIAnalysisResponse updateRecommendationStatus(
            @PathVariable UUID incidentId,
            @PathVariable int priority,
            @RequestBody AIRecommendationStatusRequest request) {

        AIAnalysis analysis = repository.findByIncidentId(incidentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "AI analysis not found: " + incidentId));

        try {
            List<AIAnalysisResponse.Recommendation> recommendations = objectMapper.readValue(
                    analysis.getRecommendations(),
                    new TypeReference<>() {
                    });

            boolean found = false;
            List<AIAnalysisResponse.Recommendation> updatedRecommendations = new java.util.ArrayList<>();
            for (AIAnalysisResponse.Recommendation recommendation : recommendations) {
                if (recommendation.priority() == priority
                        && recommendation.command().equals(request.command())) {
                    updatedRecommendations.add(new AIAnalysisResponse.Recommendation(
                            recommendation.priority(),
                            recommendation.action(),
                            recommendation.command(),
                            request.status(),
                            recommendation.executionOutput(),
                            recommendation.executionError()));
                    found = true;
                } else {
                    updatedRecommendations.add(recommendation);
                }
            }

            
            if (!found) {
                throw new IllegalArgumentException(
                        "Recommendation not found for incident: " + incidentId);
            }

            analysis.setRecommendations(objectMapper.writeValueAsString(updatedRecommendations));
            return toResponse(repository.save(analysis));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to update AI recommendation status", e);
        }
    }

    private AIAnalysisResponse toResponse(AIAnalysis analysis) {

        try {
            List<AIAnalysisResponse.Recommendation> recommendations = objectMapper.readValue(
                    analysis.getRecommendations(),
                    new TypeReference<>() {
                    });

            return new AIAnalysisResponse(
                    analysis.getIncidentId(),
                    analysis.getDeviceId(),
                    analysis.getTenantId(),
                    analysis.getSeverity(),
                    analysis.getProblem(),
                    analysis.getRootCause(),
                    analysis.getConfidence(),
                    analysis.getImpact(),
                    recommendations,
                    analysis.getProblemStatus(),
                    analysis.getVerificationConfidence(),
                    analysis.getVerificationExplanation(),
                    analysis.getVerificationNextAction(),
                    analysis.getCreatedAt());


        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse AI recommendations", e);
        }
    }
}