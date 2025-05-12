package com.healthgig.platform.modules.users.model;

import com.healthgig.platform.shared.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Profile entity for workers with healthcare-specific details.
 */
@Entity
@Table(name = "worker_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column
    private String bio;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String zipCode;

    @Column
    private String country;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> specialties = new HashSet<>();

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "license_state")
    private String licenseState;

    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;

    @Column(name = "highest_education")
    @Enumerated(EnumType.STRING)
    private Education highestEducation;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "background_check_verified")
    private Boolean backgroundCheckVerified;

    @Column(name = "credential_verified")
    private Boolean credentialVerified;

    public enum Education {
        HIGH_SCHOOL,
        ASSOCIATES,
        BACHELORS,
        MASTERS,
        DOCTORATE,
        OTHER
    }
}