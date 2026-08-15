package com.realtime_monitoring.query_service.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teams_Overview")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamOverview extends BaseEntityOverview {

    private String name;

    private String description;

    private UUID tenantId;
    private String tenantName;
    private int userNumber;
    private UUID teamLeaderId;
    private String teamLeaderName; 
}
