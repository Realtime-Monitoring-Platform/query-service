package com.realtime_monitoring.query_service.modelt;
import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="permissions")
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends BaseEntity {
    

    private String name;
    private String description;
    private String resource;
    private String action;
    
}
