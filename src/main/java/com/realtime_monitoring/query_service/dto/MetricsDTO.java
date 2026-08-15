package com.realtime_monitoring.query_service.dto;


import java.util.UUID;

import lombok.Data;

@Data
public class MetricsDTO {
    private UUID device_id;
    private UUID tenant_id;
    private double cpu;
    private double ram;
}
