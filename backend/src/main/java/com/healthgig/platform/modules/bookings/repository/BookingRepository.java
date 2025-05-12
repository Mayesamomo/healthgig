package com.healthgig.platform.modules.bookings.repository;

import com.healthgig.platform.modules.bookings.model.Booking;
import com.healthgig.platform.modules.jobs.model.Job;
import com.healthgig.platform.modules.users.model.WorkerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByWorker(WorkerProfile worker, Pageable pageable);
    Page<Booking> findByJob(Job job, Pageable pageable);
    Page<Booking> findByStatus(Booking.BookingStatus status, Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE b.job.employer.id = :employerId")
    Page<Booking> findByEmployerId(Long employerId, Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE b.worker.id = :workerId AND b.status = :status")
    Page<Booking> findByWorkerIdAndStatus(Long workerId, Booking.BookingStatus status, Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE b.job.employer.id = :employerId AND b.status = :status")
    Page<Booking> findByEmployerIdAndStatus(Long employerId, Booking.BookingStatus status, Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE b.startTime >= :startDate AND b.endTime <= :endDate")
    Page<Booking> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}