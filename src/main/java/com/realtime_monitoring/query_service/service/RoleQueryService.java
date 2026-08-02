package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.model.RoleOverview;
import com.realtime_monitoring.query_service.repository.RoleOverviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleQueryService {

    private final RoleOverviewRepository roleOverviewRepository;

    public RoleQueryService(RoleOverviewRepository roleOverviewRepository) {
        this.roleOverviewRepository = roleOverviewRepository;
    }

    public List<RoleOverview> getAllRoles() {
        return roleOverviewRepository.findAll();
    }

    public Page<RoleOverview> getAllRoles(Pageable pageable) {
        return roleOverviewRepository.findAll(pageable);
    }

    public Optional<RoleOverview> getRoleById(UUID id) {
        return roleOverviewRepository.findById(id);
    }

    public Optional<RoleOverview> getRoleByName(String name) {
        return roleOverviewRepository.findByName(name);
    }
}