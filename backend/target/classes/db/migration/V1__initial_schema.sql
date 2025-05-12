-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create worker_profiles table
CREATE TABLE worker_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    bio TEXT,
    date_of_birth DATE,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    years_of_experience INTEGER,
    license_number VARCHAR(100),
    license_state VARCHAR(50),
    license_expiry_date DATE,
    highest_education VARCHAR(50),
    hourly_rate DECIMAL(10, 2),
    profile_picture_url VARCHAR(255),
    background_check_verified BOOLEAN,
    credential_verified BOOLEAN,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create worker_specialties table
CREATE TABLE worker_specialties (
    worker_profile_id BIGINT REFERENCES worker_profiles(id),
    specialty VARCHAR(100),
    PRIMARY KEY (worker_profile_id, specialty)
);

-- Create employer_profiles table
CREATE TABLE employer_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    organization_name VARCHAR(255) NOT NULL,
    organization_type VARCHAR(100),
    description TEXT,
    website VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    tax_id VARCHAR(100),
    business_license VARCHAR(100),
    logo_url VARCHAR(255),
    verified BOOLEAN,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create jobs table
CREATE TABLE jobs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    employer_id BIGINT NOT NULL REFERENCES employer_profiles(id),
    job_type VARCHAR(100),
    required_experience_years INTEGER,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL,
    hourly_rate DECIMAL(10, 2) NOT NULL,
    location VARCHAR(255),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    remote_possible BOOLEAN,
    application_deadline TIMESTAMP,
    max_applicants INTEGER,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create job_required_skills table
CREATE TABLE job_required_skills (
    job_id BIGINT REFERENCES jobs(id),
    skill VARCHAR(100),
    PRIMARY KEY (job_id, skill)
);

-- Create bookings table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    job_id BIGINT NOT NULL REFERENCES jobs(id),
    worker_id BIGINT NOT NULL REFERENCES worker_profiles(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    actual_start_time TIMESTAMP,
    actual_end_time TIMESTAMP,
    hours_worked DECIMAL(10, 2),
    hourly_rate DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2),
    checkin_latitude DECIMAL(10, 8),
    checkin_longitude DECIMAL(11, 8),
    checkout_latitude DECIMAL(10, 8),
    checkout_longitude DECIMAL(11, 8),
    worker_notes TEXT,
    employer_notes TEXT,
    status VARCHAR(50) NOT NULL,
    cancellation_reason TEXT,
    cancelled_by VARCHAR(100),
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create payments table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    transaction_id VARCHAR(255),
    stripe_payment_intent_id VARCHAR(255),
    amount DECIMAL(10, 2) NOT NULL,
    platform_fee DECIMAL(10, 2),
    payment_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    receipt_url VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create reviews table
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    reviewer_id BIGINT NOT NULL REFERENCES users(id),
    reviewee_id BIGINT NOT NULL REFERENCES users(id),
    rating INTEGER NOT NULL,
    comment TEXT,
    review_type VARCHAR(50) NOT NULL,
    is_published BOOLEAN,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create notifications table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    target_url VARCHAR(255),
    type VARCHAR(50) NOT NULL,
    reference_id BIGINT,
    reference_type VARCHAR(100),
    is_read BOOLEAN,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Indexes for performance optimization
CREATE INDEX idx_jobs_employer_id ON jobs(employer_id);
CREATE INDEX idx_jobs_status ON jobs(status);
CREATE INDEX idx_jobs_location ON jobs(city, state);
CREATE INDEX idx_bookings_worker_id ON bookings(worker_id);
CREATE INDEX idx_bookings_job_id ON bookings(job_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_payments_booking_id ON payments(booking_id);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);