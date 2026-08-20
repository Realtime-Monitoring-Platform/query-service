package com.realtime_monitoring.query_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "notifications_overview")
@Getter
@Setter
@NoArgsConstructor
public class NotificationOverview {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String type;


    
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private long createdAt;

    @Column(nullable = false)
    private boolean read;
}
