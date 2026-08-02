package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.model.DeviceOverview;
import com.realtime_monitoring.query_service.repository.DeviceOverviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceQueryService {

    private final DeviceOverviewRepository deviceOverviewRepository;

    public DeviceQueryService(DeviceOverviewRepository deviceOverviewRepository) {
        this.deviceOverviewRepository = deviceOverviewRepository;
    }

    public Optional<DeviceOverview> getDeviceById(UUID id) {
        return deviceOverviewRepository.findById(id);
    }

    public Optional<DeviceOverview> getDeviceByHostname(String hostname) {
        return deviceOverviewRepository.findByHostname(hostname);
    }

    public Page<DeviceOverview> getDevicesByTenant(UUID tenantId, Pageable pageable) {
        return deviceOverviewRepository.findByTenantId(tenantId, pageable);
    }

    public List<DeviceOverview> getDevicesByTeam(UUID teamId) {
        return deviceOverviewRepository.findByTeamId(teamId);
    }

    public Page<DeviceOverview> getAllDevices(Pageable pageable) {
        return deviceOverviewRepository.findAll(pageable);
    }
}
