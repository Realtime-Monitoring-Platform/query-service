package com.realtime_monitoring.query_service.model;
import java.time.LocalDateTime;
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

    private UUID roleId;
    private String roleName;

    

}