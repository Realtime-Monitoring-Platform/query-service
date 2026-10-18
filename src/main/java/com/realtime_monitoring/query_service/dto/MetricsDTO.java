package com.realtime_monitoring.query_service.dto;


import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class MetricsDTO {
    private UUID device_id;
    private UUID tenant_id;
    private Instant timestamp;
    private double cpu;
    private double ram;
    private MemoryMetricsDTO memory;
}
