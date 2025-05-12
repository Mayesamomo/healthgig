package com.healthgig.platform.modules.jobs.repository;

import com.healthgig.platform.modules.jobs.model.Job;
import com.healthgig.platform.modules.users.model.EmployerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByEmployer(EmployerProfile employer, Pageable pageable);
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = :status AND j.startDateTime >= :startDate AND j.startDateTime <= :endDate")
    Page<Job> findByStatusAndDateRange(Job.JobStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = :status AND (LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> searchByKeyword(String keyword, Job.JobStatus status, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = :status AND j.city = :city")
    Page<Job> findByStatusAndCity(Job.JobStatus status, String city, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = :status AND j.jobType = :jobType")
    Page<Job> findByStatusAndJobType(Job.JobStatus status, Job.JobType jobType, Pageable pageable);
}