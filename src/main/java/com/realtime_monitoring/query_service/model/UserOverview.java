package com.realtime_monitoring.query_service.model;



import java.util.UUID;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usersOverview")
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

    //private LocalDateTime lastLogin;

    private UUID tenantId;

    // @ManyToMany
    // @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    // private Set<Role> roles = new HashSet();

    // @ManyToMany
    // @JoinTable(name = "user_teams", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    // private Set<Team> teams = new HashSet<>();

}