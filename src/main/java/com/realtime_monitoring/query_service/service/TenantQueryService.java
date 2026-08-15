package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.repository.TenantOverviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantQueryService {

    private final TenantOverviewRepository tenantOverviewRepository;

    public TenantQueryService(TenantOverviewRepository tenantOverviewRepository) {
        this.tenantOverviewRepository = tenantOverviewRepository;
    }

    public Optional<TenantOverview> getTenantById(UUID id) {
        return tenantOverviewRepository.findById(id);
    }
    public List<TenantOverview> getAllTenants() {
        return tenantOverviewRepository.findAll();
    }

    // public Optional<TenantOverview> getTenantByName(String name) {
    //     return tenantOverviewRepository.findByName(name);
    // }

    public Page<TenantOverview> getAllTenants(Pageable pageable) {
        return tenantOverviewRepository.findAll(pageable);
    }

    // public boolean existsByName(String name) {
    //     return tenantOverviewRepository.existsByName(name);
    // }
}
