package com.healthgig.platform.modules.jobs.model;

import com.healthgig.platform.modules.users.model.EmployerProfile;
import com.healthgig.platform.shared.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Job entity representing healthcare gigs posted by employers.
 */
@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private EmployerProfile employer;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> requiredSkills = new HashSet<>();

    @Column(name = "required_experience_years")
    private Integer requiredExperienceYears;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "hourly_rate", nullable = false)
    private Double hourlyRate;

    @Column
    private String location;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(name = "remote_possible")
    private Boolean remotePossible;

    @Column(name = "application_deadline")
    private LocalDateTime applicationDeadline;

    @Column(name = "max_applicants")
    private Integer maxApplicants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    public enum JobType {
        NURSE_RN,
        NURSE_LPN,
        NURSE_AIDE,
        PHYSICIAN,
        PHYSICIAN_ASSISTANT,
        DENTIST,
        DENTAL_HYGIENIST,
        PHYSICAL_THERAPIST,
        OCCUPATIONAL_THERAPIST,
        RESPIRATORY_THERAPIST,
        PHARMACIST,
        PHARMACY_TECHNICIAN,
        MEDICAL_ASSISTANT,
        MEDICAL_RECEPTIONIST,
        OTHER
    }

    public enum JobStatus {
        DRAFT,
        OPEN,
        FILLED,
        CANCELLED,
        COMPLETED,
        EXPIRED
    }
}