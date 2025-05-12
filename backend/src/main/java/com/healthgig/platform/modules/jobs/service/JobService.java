package com.healthgig.platform.modules.jobs.service;

import com.healthgig.platform.modules.jobs.dto.JobDto;
import com.healthgig.platform.modules.jobs.model.Job;
import com.healthgig.platform.modules.jobs.repository.JobRepository;
import com.healthgig.platform.modules.users.model.EmployerProfile;
import com.healthgig.platform.modules.users.repository.EmployerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final EmployerProfileRepository employerProfileRepository;

    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    public Page<Job> getOpenJobs(Pageable pageable) {
        return jobRepository.findByStatus(Job.JobStatus.OPEN, pageable);
    }

    public Page<Job> getJobsByEmployer(Long employerId, Pageable pageable) {
        EmployerProfile employer = employerProfileRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        return jobRepository.findByEmployer(employer, pageable);
    }

    public Page<Job> searchJobs(String keyword, String city, String jobType, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return jobRepository.searchByKeyword(keyword, Job.JobStatus.OPEN, pageable);
        } else if (city != null && !city.isEmpty()) {
            return jobRepository.findByStatusAndCity(Job.JobStatus.OPEN, city, pageable);
        } else if (jobType != null && !jobType.isEmpty()) {
            try {
                Job.JobType type = Job.JobType.valueOf(jobType);
                return jobRepository.findByStatusAndJobType(Job.JobStatus.OPEN, type, pageable);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid job type");
            }
        } else {
            return getOpenJobs(pageable);
        }
    }

    public Page<Job> getJobsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return jobRepository.findByStatusAndDateRange(Job.JobStatus.OPEN, startDate, endDate, pageable);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    @Transactional
    public Job createJob(JobDto jobDto) {
        EmployerProfile employer = employerProfileRepository.findById(jobDto.getEmployerId())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setEmployer(employer);
        
        if (jobDto.getJobType() != null) {
            job.setJobType(Job.JobType.valueOf(jobDto.getJobType()));
        }
        
        job.setRequiredExperienceYears(jobDto.getRequiredExperienceYears());
        job.setStartDateTime(jobDto.getStartDateTime());
        job.setEndDateTime(jobDto.getEndDateTime());
        job.setHourlyRate(jobDto.getHourlyRate());
        job.setLocation(jobDto.getLocation());
        job.setAddress(jobDto.getAddress());
        job.setCity(jobDto.getCity());
        job.setState(jobDto.getState());
        job.setZipCode(jobDto.getZipCode());
        job.setLatitude(jobDto.getLatitude());
        job.setLongitude(jobDto.getLongitude());
        job.setRemotePossible(jobDto.getRemotePossible());
        job.setApplicationDeadline(jobDto.getApplicationDeadline());
        job.setMaxApplicants(jobDto.getMaxApplicants());
        
        // Set default status if not provided
        if (jobDto.getStatus() != null) {
            job.setStatus(Job.JobStatus.valueOf(jobDto.getStatus()));
        } else {
            job.setStatus(Job.JobStatus.OPEN);
        }
        
        // Add required skills if provided
        if (jobDto.getRequiredSkills() != null) {
            job.setRequiredSkills(new HashSet<>(jobDto.getRequiredSkills()));
        }

        return jobRepository.save(job);
    }

    @Transactional
    public Job updateJob(Long id, JobDto jobDto) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Update fields
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        
        if (jobDto.getJobType() != null) {
            job.setJobType(Job.JobType.valueOf(jobDto.getJobType()));
        }
        
        job.setRequiredExperienceYears(jobDto.getRequiredExperienceYears());
        job.setStartDateTime(jobDto.getStartDateTime());
        job.setEndDateTime(jobDto.getEndDateTime());
        job.setHourlyRate(jobDto.getHourlyRate());
        job.setLocation(jobDto.getLocation());
        job.setAddress(jobDto.getAddress());
        job.setCity(jobDto.getCity());
        job.setState(jobDto.getState());
        job.setZipCode(jobDto.getZipCode());
        job.setLatitude(jobDto.getLatitude());
        job.setLongitude(jobDto.getLongitude());
        job.setRemotePossible(jobDto.getRemotePossible());
        job.setApplicationDeadline(jobDto.getApplicationDeadline());
        job.setMaxApplicants(jobDto.getMaxApplicants());
        
        if (jobDto.getStatus() != null) {
            job.setStatus(Job.JobStatus.valueOf(jobDto.getStatus()));
        }
        
        // Update required skills if provided
        if (jobDto.getRequiredSkills() != null) {
            job.setRequiredSkills(new HashSet<>(jobDto.getRequiredSkills()));
        }

        return jobRepository.save(job);
    }

    @Transactional
    public Job updateJobStatus(Long id, String status) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        try {
            job.setStatus(Job.JobStatus.valueOf(status));
            return jobRepository.save(job);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid job status");
        }
    }

    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Instead of deleting, mark as cancelled
        job.setStatus(Job.JobStatus.CANCELLED);
        jobRepository.save(job);
    }
}