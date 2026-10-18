package com.realtime_monitoring.query_service.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Data;

/**
 * Latest reading of a device: scalar cpu/ram usage together with the memory, disk and
 * network breakdown collected by the agent in the very same metrics message.
 */
@Data
public class MetricsSnapshotDTO {

    private UUID device_id;
    private UUID tenant_id;
    private Instant timestamp;
    private Double cpu;
    private Double ram;
    private MemoryMetricsDTO memory;
    private List<DiskMetricsDTO> disks;
    private List<NetworkMetricsDTO> network;
}