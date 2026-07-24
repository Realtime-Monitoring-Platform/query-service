package com.realtime_monitoring.query_service.kafka;

public interface EventTopics {
    String USER_CREATED = "user-creation";
    String USER_UPDATED = "user-update";
    String TENANT_CREATED = "Tenant-creation";
    String TENANT_UPDATED = "Tenant-update";
    String DEVICE_CREATED = "device-creation";
    String DEVICE_UPDATED = "device-update";
    String TEAM_CREATED = "team-creation";
    String TEAM_UPDATED = "team-update";
    String DLQ_SUFFIX = ".DLQ";
}
