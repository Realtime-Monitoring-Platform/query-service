package com.realtime_monitoring.query_service.event;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;


public record AIAnalysisResultEvent(
        UUID incidentId,
        UUID deviceId,
        UUID tenantId,
        String severity,
        String problem,
        String rootCause,
        double confidence,
        String impact,
        List<Recommendation> recommendations) {

    public record Recommendation(
            int priority,
            String action,
            String command,
            @JsonProperty("execution_status") String executionStatus,
            @JsonProperty("execution_output") String executionOutput,
            @JsonProperty("execution_error") String executionError) {
    }
}