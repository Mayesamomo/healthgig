package com.healthgig.platform.modules.jobs.controller;

import com.healthgig.platform.modules.jobs.dto.JobDto;
import com.healthgig.platform.modules.jobs.model.Job;
import com.healthgig.platform.modules.jobs.service.JobService;
import com.healthgig.platform.modules.users.model.User;
import com.healthgig.platform.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Job posting and discovery APIs")
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all jobs", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDateTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return ResponseEntity.ok(jobService.getAllJobs(pageable));
    }

    @GetMapping("/open")
    @Operation(summary = "Get open jobs", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Job>> getOpenJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDateTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return ResponseEntity.ok(jobService.getOpenJobs(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Job>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String jobType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDateTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return ResponseEntity.ok(jobService.searchJobs(keyword, city, jobType, pageable));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get jobs by date range", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Job>> getJobsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.getJobsByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/employer/{employerId}")
    @Operation(summary = "Get jobs by employer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Job>> getJobsByEmployer(
            @PathVariable Long employerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.getJobsByEmployer(employerId, pageable));
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Get current employer's jobs", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Job>> getCurrentEmployerJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getRole() == User.UserRole.EMPLOYER) {
                // Get the employer profile id
                return userService.getEmployerProfile(user.getId())
                        .map(profile -> {
                            Pageable pageable = PageRequest.of(page, size);
                            return ResponseEntity.ok(jobService.getJobsByEmployer(profile.getId(), pageable));
                        })
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Create a job", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Job> createJob(@Valid @RequestBody JobDto jobDto) {
        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getRole() == User.UserRole.EMPLOYER) {
                // Get the employer profile and set the ID on the job DTO
                return userService.getEmployerProfile(user.getId())
                        .map(profile -> {
                            jobDto.setEmployerId(profile.getId());
                            return ResponseEntity.status(HttpStatus.CREATED)
                                    .body(jobService.createJob(jobDto));
                        })
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @Operation(summary = "Update a job", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id, 
            @Valid @RequestBody JobDto jobDto) {
        
        // Check if the job exists and if the current user is the employer who created it
        Optional<Job> jobOpt = jobService.getJobById(id);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Job job = jobOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Admin can update any job
            if (user.getRole() == User.UserRole.ADMIN) {
                return ResponseEntity.ok(jobService.updateJob(id, jobDto));
            }
            
            // Employer can only update their own jobs
            if (user.getRole() == User.UserRole.EMPLOYER) {
                return userService.getEmployerProfile(user.getId())
                        .filter(profile -> profile.getId().equals(job.getEmployer().getId()))
                        .map(profile -> ResponseEntity.ok(jobService.updateJob(id, jobDto)))
                        .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @Operation(summary = "Update job status", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Job> updateJobStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        
        // Check if the job exists and if the current user is the employer who created it
        Optional<Job> jobOpt = jobService.getJobById(id);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Job job = jobOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Admin can update any job
            if (user.getRole() == User.UserRole.ADMIN) {
                return ResponseEntity.ok(jobService.updateJobStatus(id, status));
            }
            
            // Employer can only update their own jobs
            if (user.getRole() == User.UserRole.EMPLOYER) {
                return userService.getEmployerProfile(user.getId())
                        .filter(profile -> profile.getId().equals(job.getEmployer().getId()))
                        .map(profile -> ResponseEntity.ok(jobService.updateJobStatus(id, status)))
                        .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')")
    @Operation(summary = "Delete a job", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> deleteJob(@PathVariable Long id) {
        // Check if the job exists and if the current user is the employer who created it
        Optional<Job> jobOpt = jobService.getJobById(id);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Job job = jobOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Admin can delete any job
            if (user.getRole() == User.UserRole.ADMIN) {
                jobService.deleteJob(id);
                return ResponseEntity.noContent().build();
            }
            
            // Employer can only delete their own jobs
            if (user.getRole() == User.UserRole.EMPLOYER) {
                return userService.getEmployerProfile(user.getId())
                        .filter(profile -> profile.getId().equals(job.getEmployer().getId()))
                        .map(profile -> {
                            jobService.deleteJob(id);
                            return ResponseEntity.<Void>noContent().build();
                        })
                        .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}