package com.realtime_monitoring.query_service.controller;

import com.realtime_monitoring.query_service.model.NotificationOverview;
import com.realtime_monitoring.query_service.service.NotificationQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/query/notifications")
@RequiredArgsConstructor
public class NotificationQueryController {

    private final NotificationQueryService notificationQueryService;


    @GetMapping("/{id}")
    public ResponseEntity<NotificationOverview> getById(@PathVariable String id) {
        return notificationQueryService.getById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationOverview>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(notificationQueryService.getByUserId(userId));
    }

    

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<NotificationOverview>> getByTenantId(@PathVariable String tenantId) {
        return ResponseEntity.ok(notificationQueryService.getByTenantId(tenantId));
    }

    @GetMapping("/user/me")
    public ResponseEntity<List<NotificationOverview>> getByCurrentUser(HttpServletRequest request) {
        return ResponseEntity.ok(notificationQueryService.getByUserId(request.getHeader("X-User-Id")));
    }

    @GetMapping("/user/me/unread")
    public ResponseEntity<List<NotificationOverview>> getUnreadByCurrentUser(HttpServletRequest request) {
        return ResponseEntity.ok(notificationQueryService.getUnreadByUserId(request.getHeader("X-User-Id")));
    }


    @GetMapping("/user/me/unread/count")
    public ResponseEntity<Long> getUnreadCountByCurrentUser(HttpServletRequest request) {
        return ResponseEntity.ok(notificationQueryService.getUnreadCountByUserId(request.getHeader("X-User-Id")));
    }
}
