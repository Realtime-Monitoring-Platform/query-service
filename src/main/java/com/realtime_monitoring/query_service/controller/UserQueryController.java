package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.dto.UserOverviewResponse;
import com.realtime_monitoring.query_service.service.UserQueryService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
@RequestMapping("/api/v1/query/users")
public class UserQueryController {
    
    private final UserQueryService userQueryService;
    // private final AuthenticationService service;
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

    // @GetMapping("/tenant/{tenantId}")
    // public ResponseEntity<Page<UserOverviewResponse>> getAllUsersByTenantId(
    //         @PathVariable UUID tenantId,
    //         @PageableDefault(page = 0, size = 10) Pageable pageable,
    //         HttpServletRequest request
    //     ) {
    //             System.out.println("X-User-Id: " + request.getHeader("X-User-Id"));
    // System.out.println("X-User-Email: " + request.getHeader("X-User-Email"));
    // System.out.println("X-User-Role: " + request.getHeader("X-User-Role"));
    // System.out.println("X-User-Tenant-Id: " + request.getHeader("X-User-Tenant-Id"));
    // System.out.println("X-User-Name: " + request.getHeader("X-User-Name"));
    //     Page<UserOverviewResponse> users = userQueryService.getAllUsers(pageable)
    //             .map(UserOverviewResponse::from);
    //     return ResponseEntity.ok(users);
    // }

    @GetMapping
    public ResponseEntity<Page<UserOverviewResponse>> getAllUsers(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            HttpServletRequest request
        ) {
                System.out.println("X-User-Id: " + request.getHeader("X-User-Id"));
    System.out.println("X-User-Email: " + request.getHeader("X-User-Email"));
    System.out.println("X-User-Role: " + request.getHeader("X-User-Role"));
    System.out.println("X-User-Tenant-Id: " + request.getHeader("X-User-Tenant-Id"));
    System.out.println("X-User-Name: " + request.getHeader("X-User-Name"));
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

    @GetMapping("/me")
    public ResponseEntity<UserOverviewResponse> getCurrentUser(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null) {
            return ResponseEntity.status(401).build();
        }
        return userQueryService.getUserByEmail(email)
                .map(UserOverviewResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//       @GetMapping("/mee")
//   public ResponseEntity<Map<String, Object>> getCurrentUser() {
//     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//     Map<String, Object> userInfo = service.getCurrentUserInfo(authentication.getName());
//     userInfo.put("authorities", authentication.getAuthorities());
//     return ResponseEntity.ok(userInfo);
//   }
}
