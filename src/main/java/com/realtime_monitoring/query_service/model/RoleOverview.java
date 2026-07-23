package com.realtime_monitoring.query_service.model;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name="roles_Overview")
@AllArgsConstructor
@NoArgsConstructor
public class RoleOverview extends BaseEntityOverview{
    
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
        name="role_permissions",
        joinColumns=@JoinColumn(name="role_id"),
        inverseJoinColumns=@JoinColumn(name="permission_id")
    )
    
    private Set<PermissionOverview> permissions= new HashSet<>();

}
