package com.realtime_monitoring.query_service.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "device_Overview")
@AllArgsConstructor
@NoArgsConstructor
@EnableJpaAuditing
public class DeviceOverview {
    @Id

    private UUID id;

    private UUID tenantId;
    private String tenantName;

    private UUID teamId;
    private String teamName;

    private UUID assignedUserId;
    private String assignedUserName;
    
    private String deviceName;

    private String model;

    private String manufacturer;

    private String Description;
    private String hostname;

    private String ipAddress;

    private String macAddress;

    @Column(nullable = true, unique = true, updatable = false)
    private String deviceIdentifier;

    private String location;

    @Enumerated(EnumType.STRING)
    private DeviceStatusOverview status;


    private String osName;
    private String osVersion;
    private String kernelVersion;
    private Long cpuCount;
    private Long totalMemoryKb;
    private LocalDateTime lastSeen;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}