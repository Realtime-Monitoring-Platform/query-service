package com.realtime_monitoring.query_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Usage of a single disk/partition of the device (values expressed in bytes).
 */
@Data
public class DiskMetricsDTO {

    private String name;

    @JsonProperty("mount_point")
    private String mountPoint;

    @JsonProperty("file_system")
    private String fileSystem;

    @JsonProperty("total_bytes")
    private Long totalBytes;

    @JsonProperty("available_bytes")
    private Long availableBytes;

    @JsonProperty("used_bytes")
    private Long usedBytes;

    @JsonProperty("used_percent")
    private Double usedPercent;

    @JsonProperty("read_bytes")
    private Long readBytes;

    @JsonProperty("written_bytes")
    private Long writtenBytes;
}