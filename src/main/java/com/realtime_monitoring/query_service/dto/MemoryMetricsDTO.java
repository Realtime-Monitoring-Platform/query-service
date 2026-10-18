package com.realtime_monitoring.query_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Memory breakdown reported by the device agent (values expressed in kibibytes).
 */
@Data
public class MemoryMetricsDTO {

    @JsonProperty("total_kb")
    private Long totalKb;

    @JsonProperty("used_kb")
    private Long usedKb;

    @JsonProperty("free_kb")
    private Long freeKb;

    @JsonProperty("available_kb")
    private Long availableKb;

    @JsonProperty("swap_total_kb")
    private Long swapTotalKb;

    @JsonProperty("swap_used_kb")
    private Long swapUsedKb;
}