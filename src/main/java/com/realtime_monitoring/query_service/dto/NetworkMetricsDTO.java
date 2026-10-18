package com.realtime_monitoring.query_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Traffic counters of a single network interface (values expressed in bytes).
 */
@Data
public class NetworkMetricsDTO {

    @JsonProperty("interface")
    private String interfaceName;

    @JsonProperty("received_bytes")
    private Long receivedBytes;

    @JsonProperty("transmitted_bytes")
    private Long transmittedBytes;
}