package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.dto.TeamOverviewResponse;
import com.realtime_monitoring.query_service.service.TeamQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/teams")
public class TeamQueryController {

    private final TeamQueryService teamQueryService;
    
    public TeamQueryController(TeamQueryService teamQueryService) {
        this.teamQueryService = teamQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamOverviewResponse> getTeamById(@PathVariable UUID id) {
        return teamQueryService.getTeamById(id)
                .map(TeamOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<TeamOverviewResponse>> getAllTeams(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TeamOverviewResponse> teams = teamQueryService.getAllTeams(pageable)
                .map(TeamOverviewResponse::from);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<TeamOverviewResponse>> getTeamsByTenant(@PathVariable UUID tenantId) {
        List<TeamOverviewResponse> teams = teamQueryService.getTeamsByTenant(tenantId)
                .stream()
                .map(TeamOverviewResponse::from)
                .toList();
        return ResponseEntity.ok(teams);
    }
}
