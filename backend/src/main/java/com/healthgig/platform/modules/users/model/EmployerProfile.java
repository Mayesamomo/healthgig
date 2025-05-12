package com.healthgig.platform.modules.users.model;

import com.healthgig.platform.shared.model.BaseEntity;
import jakarta.persistence.Column;
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

/**
 * Profile entity for employers with facility/healthcare organization details.
 */
@Entity
@Table(name = "employer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "organization_type")
    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    @Column
    private String description;

    @Column
    private String website;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column
    private String country;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "business_license")
    private String businessLicense;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "verified")
    private Boolean verified;

    public enum OrganizationType {
        HOSPITAL,
        CLINIC,
        NURSING_HOME,
        HOME_HEALTH_AGENCY,
        PHYSICIANS_OFFICE,
        DENTAL_OFFICE,
        PHARMACY,
        OTHER
    }
}