package com.realtime_monitoring.query_service.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
@Entity
@Table(name = "tenant_Overview")
@EntityListeners(AuditingEntityListener.class)
@Builder
@RequiredArgsConstructor
public class TenantOverview {

    @Id
    private UUID id;

    private String name;

    private String companyName;

    private String email;

    private String phone;

    private String status;

    private int userNumber;

    private int deviceNumber;

    private int teamNumber;

    private int alertNumber;

    private  UUID AdminId;

    private String adminName;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}