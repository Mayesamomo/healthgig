package com.healthgig.platform.modules.users.repository;

import com.healthgig.platform.modules.users.model.EmployerProfile;
import com.healthgig.platform.modules.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    Optional<EmployerProfile> findByUser(User user);
    Optional<EmployerProfile> findByUserId(Long userId);
}