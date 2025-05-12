package com.healthgig.platform.modules.payments.repository;

import com.healthgig.platform.modules.payments.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByBookingId(Long bookingId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.booking.worker.id = :workerId")
    Page<Payment> findByWorkerId(Long workerId, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE p.booking.job.employer.id = :employerId")
    Page<Payment> findByEmployerId(Long employerId, Pageable pageable);
    
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}