package com.healthgig.platform.shared.config;

import com.healthgig.platform.modules.users.model.EmployerProfile;
import com.healthgig.platform.modules.users.model.User;
import com.healthgig.platform.modules.users.model.WorkerProfile;
import com.healthgig.platform.modules.users.repository.EmployerProfileRepository;
import com.healthgig.platform.modules.users.repository.UserRepository;
import com.healthgig.platform.modules.users.repository.WorkerProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
@Profile("dev") // Only run in development mode
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            WorkerProfileRepository workerProfileRepository,
            EmployerProfileRepository employerProfileRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if data already exists
            if (userRepository.count() > 0) {
                System.out.println("Database already initialized, skipping data creation");
                return;
            }
            
            System.out.println("Initializing database with sample data...");
            
            // Create admin user
            User admin = new User();
            admin.setEmail("admin@healthgig.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPhoneNumber("1234567890");
            admin.setRole(User.UserRole.ADMIN);
            admin.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(admin);
            
            // Create worker users
            User worker1 = new User();
            worker1.setEmail("nurse@example.com");
            worker1.setPassword(passwordEncoder.encode("password123"));
            worker1.setFirstName("Mary");
            worker1.setLastName("Johnson");
            worker1.setPhoneNumber("5551234567");
            worker1.setRole(User.UserRole.WORKER);
            worker1.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(worker1);
            
            User worker2 = new User();
            worker2.setEmail("doctor@example.com");
            worker2.setPassword(passwordEncoder.encode("password123"));
            worker2.setFirstName("John");
            worker2.setLastName("Smith");
            worker2.setPhoneNumber("5559876543");
            worker2.setRole(User.UserRole.WORKER);
            worker2.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(worker2);
            
            // Create employer users
            User employer1 = new User();
            employer1.setEmail("hospital@example.com");
            employer1.setPassword(passwordEncoder.encode("password123"));
            employer1.setFirstName("Sarah");
            employer1.setLastName("Davis");
            employer1.setPhoneNumber("5557654321");
            employer1.setRole(User.UserRole.EMPLOYER);
            employer1.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(employer1);
            
            User employer2 = new User();
            employer2.setEmail("clinic@example.com");
            employer2.setPassword(passwordEncoder.encode("password123"));
            employer2.setFirstName("Michael");
            employer2.setLastName("Brown");
            employer2.setPhoneNumber("5553216547");
            employer2.setRole(User.UserRole.EMPLOYER);
            employer2.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(employer2);
            
            // Create worker profiles
            WorkerProfile nurseProfile = new WorkerProfile();
            nurseProfile.setUser(worker1);
            nurseProfile.setBio("Experienced registered nurse with expertise in critical care.");
            nurseProfile.setDateOfBirth(LocalDate.of(1985, 6, 15));
            nurseProfile.setAddress("123 Main St");
            nurseProfile.setCity("New York");
            nurseProfile.setState("NY");
            nurseProfile.setZipCode("10001");
            nurseProfile.setCountry("USA");
            nurseProfile.setYearsOfExperience(8);
            nurseProfile.setLicenseNumber("RN123456");
            nurseProfile.setLicenseState("NY");
            nurseProfile.setLicenseExpiryDate(LocalDate.of(2025, 12, 31));
            nurseProfile.setHighestEducation(WorkerProfile.Education.BACHELORS);
            nurseProfile.setHourlyRate(45.00);
            nurseProfile.setBackgroundCheckVerified(true);
            nurseProfile.setCredentialVerified(true);
            
            Set<String> nurseSpecialties = new HashSet<>();
            nurseSpecialties.add("ICU");
            nurseSpecialties.add("Critical Care");
            nurseSpecialties.add("Emergency");
            nurseProfile.setSpecialties(nurseSpecialties);
            
            workerProfileRepository.save(nurseProfile);
            
            WorkerProfile doctorProfile = new WorkerProfile();
            doctorProfile.setUser(worker2);
            doctorProfile.setBio("Board-certified physician with a focus on emergency medicine.");
            doctorProfile.setDateOfBirth(LocalDate.of(1980, 3, 22));
            doctorProfile.setAddress("456 Oak Ave");
            doctorProfile.setCity("Los Angeles");
            doctorProfile.setState("CA");
            doctorProfile.setZipCode("90001");
            doctorProfile.setCountry("USA");
            doctorProfile.setYearsOfExperience(12);
            doctorProfile.setLicenseNumber("MD789012");
            doctorProfile.setLicenseState("CA");
            doctorProfile.setLicenseExpiryDate(LocalDate.of(2025, 12, 31));
            doctorProfile.setHighestEducation(WorkerProfile.Education.DOCTORATE);
            doctorProfile.setHourlyRate(85.00);
            doctorProfile.setBackgroundCheckVerified(true);
            doctorProfile.setCredentialVerified(true);
            
            Set<String> doctorSpecialties = new HashSet<>();
            doctorSpecialties.add("Emergency Medicine");
            doctorSpecialties.add("Urgent Care");
            doctorProfile.setSpecialties(doctorSpecialties);
            
            workerProfileRepository.save(doctorProfile);
            
            // Create employer profiles
            EmployerProfile hospitalProfile = new EmployerProfile();
            hospitalProfile.setUser(employer1);
            hospitalProfile.setOrganizationName("Metro General Hospital");
            hospitalProfile.setOrganizationType(EmployerProfile.OrganizationType.HOSPITAL);
            hospitalProfile.setDescription("A leading hospital providing comprehensive healthcare services.");
            hospitalProfile.setWebsite("https://metrogeneral.example.com");
            hospitalProfile.setAddress("789 Hospital Way");
            hospitalProfile.setCity("Chicago");
            hospitalProfile.setState("IL");
            hospitalProfile.setZipCode("60601");
            hospitalProfile.setCountry("USA");
            hospitalProfile.setLatitude(41.8781);
            hospitalProfile.setLongitude(-87.6298);
            hospitalProfile.setTaxId("12-3456789");
            hospitalProfile.setBusinessLicense("BL-123456");
            hospitalProfile.setVerified(true);
            
            employerProfileRepository.save(hospitalProfile);
            
            EmployerProfile clinicProfile = new EmployerProfile();
            clinicProfile.setUser(employer2);
            clinicProfile.setOrganizationName("Downtown Medical Clinic");
            clinicProfile.setOrganizationType(EmployerProfile.OrganizationType.CLINIC);
            clinicProfile.setDescription("A community clinic offering affordable healthcare services.");
            clinicProfile.setWebsite("https://downtownmedical.example.com");
            clinicProfile.setAddress("101 Clinic Street");
            clinicProfile.setCity("San Francisco");
            clinicProfile.setState("CA");
            clinicProfile.setZipCode("94105");
            clinicProfile.setCountry("USA");
            clinicProfile.setLatitude(37.7749);
            clinicProfile.setLongitude(-122.4194);
            clinicProfile.setTaxId("98-7654321");
            clinicProfile.setBusinessLicense("BL-654321");
            clinicProfile.setVerified(true);
            
            employerProfileRepository.save(clinicProfile);
            
            System.out.println("Sample data initialized successfully.");
        };
    }
}