package com.healthgig.platform.modules.users.service;

import com.healthgig.platform.modules.users.dto.UserDto;
import com.healthgig.platform.modules.users.dto.UserProfileDto;
import com.healthgig.platform.modules.users.model.EmployerProfile;
import com.healthgig.platform.modules.users.model.User;
import com.healthgig.platform.modules.users.model.WorkerProfile;
import com.healthgig.platform.modules.users.repository.EmployerProfileRepository;
import com.healthgig.platform.modules.users.repository.UserRepository;
import com.healthgig.platform.modules.users.repository.WorkerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(UserDto userDto) {
        // Check if user with this email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create and save the user
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(User.UserRole.valueOf(userDto.getRole()));
        user.setStatus(User.UserStatus.ACTIVE); // Default to active

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setPhoneNumber(userDto.getPhoneNumber());
                    // Don't update email or password here - separate endpoints for those
                    
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void updatePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public WorkerProfile createWorkerProfile(Long userId, UserProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure user is a worker
        if (user.getRole() != User.UserRole.WORKER) {
            throw new RuntimeException("Only workers can have worker profiles");
        }

        // Check if profile already exists
        if (workerProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Profile already exists for this user");
        }

        WorkerProfile profile = new WorkerProfile();
        profile.setUser(user);
        profile.setBio(profileDto.getBio());
        profile.setDateOfBirth(profileDto.getDateOfBirth());
        profile.setAddress(profileDto.getAddress());
        profile.setCity(profileDto.getCity());
        profile.setState(profileDto.getState());
        profile.setZipCode(profileDto.getZipCode());
        profile.setCountry(profileDto.getCountry());
        profile.setYearsOfExperience(profileDto.getYearsOfExperience());
        profile.setLicenseNumber(profileDto.getLicenseNumber());
        profile.setLicenseState(profileDto.getLicenseState());
        profile.setLicenseExpiryDate(profileDto.getLicenseExpiryDate());
        
        if (profileDto.getHighestEducation() != null) {
            profile.setHighestEducation(WorkerProfile.Education.valueOf(profileDto.getHighestEducation()));
        }
        
        profile.setHourlyRate(profileDto.getHourlyRate());
        profile.setProfilePictureUrl(profileDto.getProfilePictureUrl());
        
        // Set default values
        profile.setBackgroundCheckVerified(false);
        profile.setCredentialVerified(false);
        
        // Add specialties if provided
        if (profileDto.getSpecialties() != null) {
            profile.setSpecialties(profileDto.getSpecialties());
        }

        return workerProfileRepository.save(profile);
    }

    @Transactional
    public EmployerProfile createEmployerProfile(Long userId, UserProfileDto profileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure user is an employer
        if (user.getRole() != User.UserRole.EMPLOYER) {
            throw new RuntimeException("Only employers can have employer profiles");
        }

        // Check if profile already exists
        if (employerProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Profile already exists for this user");
        }

        EmployerProfile profile = new EmployerProfile();
        profile.setUser(user);
        profile.setOrganizationName(profileDto.getOrganizationName());
        
        if (profileDto.getOrganizationType() != null) {
            profile.setOrganizationType(EmployerProfile.OrganizationType.valueOf(profileDto.getOrganizationType()));
        }
        
        profile.setDescription(profileDto.getDescription());
        profile.setWebsite(profileDto.getWebsite());
        profile.setAddress(profileDto.getAddress());
        profile.setCity(profileDto.getCity());
        profile.setState(profileDto.getState());
        profile.setZipCode(profileDto.getZipCode());
        profile.setCountry(profileDto.getCountry());
        profile.setLatitude(profileDto.getLatitude());
        profile.setLongitude(profileDto.getLongitude());
        profile.setTaxId(profileDto.getTaxId());
        profile.setBusinessLicense(profileDto.getBusinessLicense());
        profile.setLogoUrl(profileDto.getLogoUrl());
        
        // Set default values
        profile.setVerified(false);

        return employerProfileRepository.save(profile);
    }

    @Transactional
    public Optional<WorkerProfile> getWorkerProfile(Long userId) {
        return workerProfileRepository.findByUserId(userId);
    }

    @Transactional
    public Optional<EmployerProfile> getEmployerProfile(Long userId) {
        return employerProfileRepository.findByUserId(userId);
    }

    @Transactional
    public WorkerProfile updateWorkerProfile(Long userId, UserProfileDto profileDto) {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Worker profile not found"));

        // Update fields
        profile.setBio(profileDto.getBio());
        profile.setDateOfBirth(profileDto.getDateOfBirth());
        profile.setAddress(profileDto.getAddress());
        profile.setCity(profileDto.getCity());
        profile.setState(profileDto.getState());
        profile.setZipCode(profileDto.getZipCode());
        profile.setCountry(profileDto.getCountry());
        profile.setYearsOfExperience(profileDto.getYearsOfExperience());
        profile.setLicenseNumber(profileDto.getLicenseNumber());
        profile.setLicenseState(profileDto.getLicenseState());
        profile.setLicenseExpiryDate(profileDto.getLicenseExpiryDate());
        
        if (profileDto.getHighestEducation() != null) {
            profile.setHighestEducation(WorkerProfile.Education.valueOf(profileDto.getHighestEducation()));
        }
        
        profile.setHourlyRate(profileDto.getHourlyRate());
        profile.setProfilePictureUrl(profileDto.getProfilePictureUrl());
        
        // Update specialties if provided
        if (profileDto.getSpecialties() != null) {
            profile.setSpecialties(profileDto.getSpecialties());
        }

        return workerProfileRepository.save(profile);
    }

    @Transactional
    public EmployerProfile updateEmployerProfile(Long userId, UserProfileDto profileDto) {
        EmployerProfile profile = employerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Employer profile not found"));

        // Update fields
        profile.setOrganizationName(profileDto.getOrganizationName());
        
        if (profileDto.getOrganizationType() != null) {
            profile.setOrganizationType(EmployerProfile.OrganizationType.valueOf(profileDto.getOrganizationType()));
        }
        
        profile.setDescription(profileDto.getDescription());
        profile.setWebsite(profileDto.getWebsite());
        profile.setAddress(profileDto.getAddress());
        profile.setCity(profileDto.getCity());
        profile.setState(profileDto.getState());
        profile.setZipCode(profileDto.getZipCode());
        profile.setCountry(profileDto.getCountry());
        profile.setLatitude(profileDto.getLatitude());
        profile.setLongitude(profileDto.getLongitude());
        profile.setTaxId(profileDto.getTaxId());
        profile.setBusinessLicense(profileDto.getBusinessLicense());
        profile.setLogoUrl(profileDto.getLogoUrl());

        return employerProfileRepository.save(profile);
    }

    @Transactional
    public void deactivateUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setStatus(User.UserStatus.INACTIVE);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void reactivateUser(Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setStatus(User.UserStatus.ACTIVE);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}