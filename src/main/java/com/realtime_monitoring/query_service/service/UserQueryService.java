package com.realtime_monitoring.query_service.service;

import com.realtime_monitoring.query_service.model.UserOverview;
import com.realtime_monitoring.query_service.repository.UserOverviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserQueryService {

    private final UserOverviewRepository userOverviewRepository;

    public UserQueryService(UserOverviewRepository userOverviewRepository) {
        this.userOverviewRepository = userOverviewRepository;
    }

    public Optional<UserOverview> getUserById(UUID id) {
        return userOverviewRepository.findById(id);
    }

    public Optional<UserOverview> getUserByEmail(String email) {
        return userOverviewRepository.findByEmail(email);
    }

    public Optional<UserOverview> getUserByUsername(String username) {
        return userOverviewRepository.findByUsername(username);
    }

    public Page<UserOverview> getUsersByTenant(UUID tenantId, Pageable pageable) {
        return userOverviewRepository.findByTenantId(tenantId, pageable);
    }

    public Page<UserOverview> getAllUsers(Pageable pageable) {
        return userOverviewRepository.findAll(pageable);
    }

    public boolean existsByEmail(String email) {
        return userOverviewRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userOverviewRepository.existsByUsername(username);
    }
}
