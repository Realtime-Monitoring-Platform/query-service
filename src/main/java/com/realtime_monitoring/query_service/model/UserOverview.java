package com.realtime_monitoring.query_service.model;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_Overview")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOverview extends BaseEntityOverview {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String avatarUrl;

    private String password;

    // private String resetPasswordToken;

    // private String verificationToken;

    private String Address;

    @Enumerated(EnumType.STRING)
    private UserStatusOverview status;

    private LocalDateTime lastLogin;


    private UUID tenantId;
    private String tenantName;

    private UUID teamId;
    private String teamName;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleOverview> roles = new HashSet();

    @ManyToMany
    @JoinTable(name = "user_teams", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<TeamOverview> teams = new HashSet<>();

}