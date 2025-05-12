package com.healthgig.platform.modules.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDto {

    private Long id;
    private Long userId;
    
    // Common fields for all profiles
    private String bio;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Double latitude;
    private Double longitude;
    
    // Worker-specific fields
    private LocalDate dateOfBirth;
    private Integer yearsOfExperience;
    private String licenseNumber;
    private String licenseState;
    private LocalDate licenseExpiryDate;
    private String highestEducation; // Enum as string
    private Double hourlyRate;
    private String profilePictureUrl;
    private Boolean backgroundCheckVerified;
    private Boolean credentialVerified;
    private Set<String> specialties;
    
    // Employer-specific fields
    private String organizationName;
    private String organizationType; // Enum as string
    private String description;
    private String website;
    private String taxId;
    private String businessLicense;
    private String logoUrl;
    private Boolean verified;
}