package com.realtime_monitoring.query_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "ai_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysis {
    @Id
    private UUID incidentId;
    private UUID deviceId;
    private UUID tenantId;
    private String severity;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String problem;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String rootCause;
    private double confidence;

    @Column(columnDefinition = "TEXT")
    private String impact;
    @Column(columnDefinition = "TEXT")
    private String recommendations;

    private String problemStatus;
    private Double verificationConfidence;

    @Column(columnDefinition = "TEXT")
    private String verificationExplanation;

    @Column(columnDefinition = "TEXT")
    private String verificationNextAction;

    private Instant createdAt;
}