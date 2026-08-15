package com.realtime_monitoring.query_service.model;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="permission_overviews")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionOverview extends BaseEntityOverview {
    

    private String name;
    private String description;
    private String resource;
    private String action;
    
}
