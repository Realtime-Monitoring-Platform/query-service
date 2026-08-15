package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.dto.TenantOverviewResponse;
import com.realtime_monitoring.query_service.model.RoleOverview;
import com.realtime_monitoring.query_service.model.TenantOverview;
import com.realtime_monitoring.query_service.service.TenantQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/tenants")
public class TenantQueryController {

    private final TenantQueryService tenantQueryService;

    public TenantQueryController(TenantQueryService tenantQueryService) {
        this.tenantQueryService = tenantQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantOverviewResponse> getTenantById(@PathVariable UUID id) {
        return tenantQueryService.getTenantById(id)
                .map(TenantOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public ResponseEntity<List<TenantOverview>> listTenants() {
        List<TenantOverview> tenants = tenantQueryService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping
    public ResponseEntity<Page<TenantOverviewResponse>> getAllTenants(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<TenantOverviewResponse> tenants = tenantQueryService.getAllTenants(pageable)
                .map(TenantOverviewResponse::from);
        return ResponseEntity.ok(tenants);
    }
}
