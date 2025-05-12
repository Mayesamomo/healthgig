-- Seed admin user (password: admin123)
INSERT INTO users (email, password, first_name, last_name, phone_number, role, status, created_at)
VALUES ('admin@healthgig.com', '$2a$10$3HtfRQqXwLp.kJ7UpopMqeRr6pKh5vvJCKdA.37z6ZSHj6IbXIPoi', 'Admin', 'User', '1234567890', 'ADMIN', 'ACTIVE', NOW());

-- Seed employer users (password: password123)
INSERT INTO users (email, password, first_name, last_name, phone_number, role, status, created_at)
VALUES 
('hospital@example.com', '$2a$10$3C6HoZPTv23c2gNVCwmHIediLxR7ZNZ3.XKyfvckO7Ec3.HdFAXBe', 'John', 'Hospital', '1234567890', 'EMPLOYER', 'ACTIVE', NOW()),
('clinic@example.com', '$2a$10$3C6HoZPTv23c2gNVCwmHIediLxR7ZNZ3.XKyfvckO7Ec3.HdFAXBe', 'Jane', 'Clinic', '9876543210', 'EMPLOYER', 'ACTIVE', NOW());

-- Seed worker users (password: password123)
INSERT INTO users (email, password, first_name, last_name, phone_number, role, status, created_at)
VALUES 
('nurse@example.com', '$2a$10$3C6HoZPTv23c2gNVCwmHIediLxR7ZNZ3.XKyfvckO7Ec3.HdFAXBe', 'Mary', 'Nurse', '5551234567', 'WORKER', 'ACTIVE', NOW()),
('doctor@example.com', '$2a$10$3C6HoZPTv23c2gNVCwmHIediLxR7ZNZ3.XKyfvckO7Ec3.HdFAXBe', 'James', 'Doctor', '5559876543', 'WORKER', 'ACTIVE', NOW());

-- Create employer profiles
INSERT INTO employer_profiles (user_id, organization_name, organization_type, description, website, address, city, state, zip_code, country, latitude, longitude, verified, created_at)
VALUES 
((SELECT id FROM users WHERE email = 'hospital@example.com'), 'Metro General Hospital', 'HOSPITAL', 'A leading hospital providing comprehensive healthcare services.', 'https://metrogeneral.example.com', '123 Hospital Way', 'Metro City', 'Metro State', '12345', 'United States', 40.7128, -74.0060, TRUE, NOW()),
((SELECT id FROM users WHERE email = 'clinic@example.com'), 'Downtown Medical Clinic', 'CLINIC', 'A community clinic offering affordable healthcare services.', 'https://downtownmedical.example.com', '456 Clinic Street', 'Metro City', 'Metro State', '12345', 'United States', 40.7178, -74.0090, TRUE, NOW());

-- Create worker profiles
INSERT INTO worker_profiles (user_id, bio, date_of_birth, address, city, state, zip_code, country, years_of_experience, license_number, license_state, license_expiry_date, highest_education, hourly_rate, background_check_verified, credential_verified, created_at)
VALUES 
((SELECT id FROM users WHERE email = 'nurse@example.com'), 'Experienced registered nurse with expertise in critical care.', '1985-06-15', '789 Nurse Avenue', 'Metro City', 'Metro State', '12345', 'United States', 8, 'RN123456', 'Metro State', '2025-12-31', 'BACHELORS', 45.00, TRUE, TRUE, NOW()),
((SELECT id FROM users WHERE email = 'doctor@example.com'), 'Board-certified physician with a focus on emergency medicine.', '1980-03-22', '101 Doctor Boulevard', 'Metro City', 'Metro State', '12345', 'United States', 12, 'MD789012', 'Metro State', '2025-12-31', 'DOCTORATE', 85.00, TRUE, TRUE, NOW());

-- Add specialties for workers
INSERT INTO worker_specialties (worker_profile_id, specialty)
VALUES 
((SELECT id FROM worker_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'nurse@example.com')), 'ICU'),
((SELECT id FROM worker_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'nurse@example.com')), 'Emergency'),
((SELECT id FROM worker_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'doctor@example.com')), 'Emergency Medicine'),
((SELECT id FROM worker_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'doctor@example.com')), 'Urgent Care');

-- Create jobs
INSERT INTO jobs (title, description, employer_id, job_type, required_experience_years, start_date_time, end_date_time, hourly_rate, location, address, city, state, zip_code, latitude, longitude, remote_possible, application_deadline, status, created_at)
VALUES 
('ICU Registered Nurse', 'We are seeking an experienced ICU registered nurse to provide critical care to patients.', 
 (SELECT id FROM employer_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'hospital@example.com')),
 'NURSE_RN', 3, NOW() + INTERVAL '3 days', NOW() + INTERVAL '3 days' + INTERVAL '8 hours', 50.00, 
 'Metro General Hospital', '123 Hospital Way', 'Metro City', 'Metro State', '12345', 40.7128, -74.0060, FALSE,
 NOW() + INTERVAL '2 days', 'OPEN', NOW()),

('Emergency Room Physician', 'Seeking an ER physician for a busy weekend shift. Board certification required.', 
 (SELECT id FROM employer_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'hospital@example.com')),
 'PHYSICIAN', 5, NOW() + INTERVAL '5 days', NOW() + INTERVAL '5 days' + INTERVAL '12 hours', 95.00, 
 'Metro General Hospital', '123 Hospital Way', 'Metro City', 'Metro State', '12345', 40.7128, -74.0060, FALSE,
 NOW() + INTERVAL '4 days', 'OPEN', NOW()),

('Clinic Nurse Practitioner', 'Seeking a nurse practitioner to provide care at our community clinic.', 
 (SELECT id FROM employer_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'clinic@example.com')),
 'NURSE_RN', 2, NOW() + INTERVAL '4 days', NOW() + INTERVAL '4 days' + INTERVAL '8 hours', 45.00, 
 'Downtown Medical Clinic', '456 Clinic Street', 'Metro City', 'Metro State', '12345', 40.7178, -74.0090, FALSE,
 NOW() + INTERVAL '3 days', 'OPEN', NOW());

-- Add required skills for jobs
INSERT INTO job_required_skills (job_id, skill)
VALUES 
((SELECT id FROM jobs WHERE title = 'ICU Registered Nurse'), 'Critical Care'),
((SELECT id FROM jobs WHERE title = 'ICU Registered Nurse'), 'Ventilator Management'),
((SELECT id FROM jobs WHERE title = 'Emergency Room Physician'), 'Emergency Medicine'),
((SELECT id FROM jobs WHERE title = 'Emergency Room Physician'), 'Trauma Care'),
((SELECT id FROM jobs WHERE title = 'Clinic Nurse Practitioner'), 'Primary Care'),
((SELECT id FROM jobs WHERE title = 'Clinic Nurse Practitioner'), 'Patient Education');

-- Create bookings
INSERT INTO bookings (job_id, worker_id, start_time, end_time, hourly_rate, status, created_at)
VALUES (
    (SELECT id FROM jobs WHERE title = 'Clinic Nurse Practitioner'),
    (SELECT id FROM worker_profiles WHERE user_id = (SELECT id FROM users WHERE email = 'nurse@example.com')),
    NOW() + INTERVAL '4 days',
    NOW() + INTERVAL '4 days' + INTERVAL '8 hours',
    45.00,
    'CONFIRMED',
    NOW()
);