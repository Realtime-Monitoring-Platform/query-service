package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.dto.PermissionOverviewResponse;
import com.realtime_monitoring.query_service.service.PermissionQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/query/permissions")
public class PermissionQueryController {

    private final PermissionQueryService permissionQueryService;

    public PermissionQueryController(PermissionQueryService permissionQueryService) {
        this.permissionQueryService = permissionQueryService;
    }

    @GetMapping
    public ResponseEntity<Page<PermissionOverviewResponse>> getAllPermissions(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<PermissionOverviewResponse> permissions = permissionQueryService.getAllPermissions(pageable)
                .map(PermissionOverviewResponse::from);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionOverviewResponse> getPermissionById(@PathVariable UUID id) {
        return permissionQueryService.getPermissionById(id)
                .map(PermissionOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PermissionOverviewResponse> getPermissionByName(@PathVariable String name) {
        return permissionQueryService.getPermissionByName(name)
                .map(PermissionOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   
    @GetMapping("/list")
    public ResponseEntity<List<PermissionOverviewResponse>> getListPermissions() {
        List<PermissionOverviewResponse> permissions = permissionQueryService.getAllPermissionsList().stream()
                .map(PermissionOverviewResponse::from)
                .toList();
        return ResponseEntity.ok(permissions);
    }
    
}