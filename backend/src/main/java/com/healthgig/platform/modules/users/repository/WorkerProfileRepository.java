package com.healthgig.platform.modules.users.repository;

import com.healthgig.platform.modules.users.model.User;
import com.healthgig.platform.modules.users.model.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long> {
    Optional<WorkerProfile> findByUser(User user);
    Optional<WorkerProfile> findByUserId(Long userId);
}