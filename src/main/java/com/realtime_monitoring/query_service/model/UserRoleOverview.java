package com.realtime_monitoring.query_service.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleOverview extends BaseEntityOverview {
    

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserOverview user;

    @ManyToOne
    @JoinColumn(name="role_id")
    private RoleOverview role;
}
