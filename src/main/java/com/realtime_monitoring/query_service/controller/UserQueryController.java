package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.dto.UserOverviewResponse;
import com.realtime_monitoring.query_service.service.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/users")
public class UserQueryController {
    
    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOverviewResponse> getUserById(@PathVariable UUID id) {
        return userQueryService.getUserById(id)
                .map(UserOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserOverviewResponse> getUserByEmail(@PathVariable String email) {
        return userQueryService.getUserByEmail(email)
                .map(UserOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<UserOverviewResponse>> getAllUsers(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<UserOverviewResponse> users = userQueryService.getAllUsers(pageable)
                .map(UserOverviewResponse::from);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserOverviewResponse>> getAllUsersList() {
        return ResponseEntity.ok(userQueryService.getAllUsers().stream().map(UserOverviewResponse::from).toList());
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<Page<UserOverviewResponse>> getUsersByTenant(
            @PathVariable UUID tenantId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<UserOverviewResponse> users = userQueryService.getUsersByTenant(tenantId, pageable)
                .map(UserOverviewResponse::from);
        return ResponseEntity.ok(users);
    }
}
