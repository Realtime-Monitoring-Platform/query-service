package com.realtime_monitoring.query_service.event;

import java.util.UUID;


public record ProblemVerificationEvent(
        UUID incidentId,
        String problemStatus,
        double confidence,
        String explanation,
        String nextAction) {
}
