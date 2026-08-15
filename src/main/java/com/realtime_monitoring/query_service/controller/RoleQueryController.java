package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.dto.RoleOverviewResponse;
import com.realtime_monitoring.query_service.model.RoleOverview;
import com.realtime_monitoring.query_service.service.RoleQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/roles")
public class RoleQueryController {

    private final RoleQueryService roleQueryService;

    public RoleQueryController(RoleQueryService roleQueryService) {
        this.roleQueryService = roleQueryService;
    }

    @GetMapping
    public ResponseEntity<Page<RoleOverviewResponse>> getAllRoles(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<RoleOverviewResponse> roles = roleQueryService.getAllRoles(pageable)
                .map(RoleOverviewResponse::from);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RoleOverview>> listRoles() {
        List<RoleOverview> roles = roleQueryService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleOverviewResponse> getRoleById(@PathVariable UUID id) {
        return roleQueryService.getRoleById(id)
                .map(RoleOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleOverviewResponse> getRoleByName(@PathVariable String name) {
        return roleQueryService.getRoleByName(name)
                .map(RoleOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}