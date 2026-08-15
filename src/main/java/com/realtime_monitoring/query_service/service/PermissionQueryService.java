package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.dto.PermissionOverviewResponse;
import com.realtime_monitoring.query_service.model.PermissionOverview;
import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.repository.PermissionOverviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PermissionQueryService {

    private final PermissionOverviewRepository permissionOverviewRepository;

    public PermissionQueryService(PermissionOverviewRepository permissionOverviewRepository) {
        this.permissionOverviewRepository = permissionOverviewRepository;
    }

    // public List<PermissionOverviewResponse> getAllPermissions() {
    //     return permissionOverviewRepository.findAll().stream()
    //             .map(PermissionOverviewResponse::from)
    //             .toList();
    // }

    public List<PermissionOverview> getAllPermissionsList() {
        return permissionOverviewRepository.findAll();
    }

    public Page<PermissionOverview> getAllPermissions(Pageable pageable) {
        return permissionOverviewRepository.findAll(pageable);
    }

    public Optional<PermissionOverview> getPermissionById(UUID id) {
        return permissionOverviewRepository.findById(id);
    }

    public Optional<PermissionOverview> getPermissionByName(String name) {
        return permissionOverviewRepository.findByName(name);
    }
}