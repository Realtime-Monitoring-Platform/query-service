package com.realtime_monitoring.query_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.realtime_monitoring.query_service.client.TenantManagementClient;
import com.realtime_monitoring.query_service.client.TenantRemoteResponse;
import com.realtime_monitoring.query_service.client.UserGrpcClient;
import com.realtime_monitoring.query_service.client.UserRemoteResponse;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {
    @Mock
    private UserGrpcClient userClient;

    @Mock
    private TenantManagementClient tenantClient;

    @InjectMocks
    private UserQueryService service;

    @Test
    void combinesUsersWithTheirTenantWithoutExposingCredentials() {
        UUID userId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(userClient.getUsers()).thenReturn(List.of(
                new UserRemoteResponse(userId, "ada", "ada@example.com", "Ada", "Lovelace", "123", null,
                        "ACTIVE", tenantId)));
        when(tenantClient.getTenant(tenantId)).thenReturn(
                new TenantRemoteResponse(tenantId, "Acme", "Acme Inc.", "support@acme.test", "456", "ACTIVE"));

        var response = service.getUsersWithTenant();

        assertThat(response).singleElement().satisfies(user -> {
            assertThat(user.getId()).isEqualTo(userId);
            assertThat(user.getTenant().getId()).isEqualTo(tenantId);
            assertThat(user).hasNoNullFieldsOrPropertiesExcept("avatarUrl");
        });
    }
}
