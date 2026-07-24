package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.model.TeamOverview;
import com.realtime_monitoring.query_service.repository.TeamOverviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamQueryService {

    private final TeamOverviewRepository teamOverviewRepository;

    public TeamQueryService(TeamOverviewRepository teamOverviewRepository) {
        this.teamOverviewRepository = teamOverviewRepository;
    }

    public Optional<TeamOverview> getTeamById(UUID id) {
        return teamOverviewRepository.findById(id);
    }

    public List<TeamOverview> getTeamsByTenant(UUID tenantId) {
        return teamOverviewRepository.findByTenantId(tenantId);
    }

    public Page<TeamOverview> getAllTeams(Pageable pageable) {
        return teamOverviewRepository.findAll(pageable);
    }
}
