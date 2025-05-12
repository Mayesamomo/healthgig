package com.healthgig.platform.modules.bookings.service;

import com.healthgig.platform.modules.bookings.dto.BookingDto;
import com.healthgig.platform.modules.bookings.model.Booking;
import com.healthgig.platform.modules.bookings.repository.BookingRepository;
import com.healthgig.platform.modules.jobs.model.Job;
import com.healthgig.platform.modules.jobs.repository.JobRepository;
import com.healthgig.platform.modules.users.model.WorkerProfile;
import com.healthgig.platform.modules.users.repository.WorkerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final JobRepository jobRepository;
    private final WorkerProfileRepository workerProfileRepository;

    public Page<Booking> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    public Page<Booking> getBookingsByWorker(Long workerId, Pageable pageable) {
        WorkerProfile worker = workerProfileRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
        return bookingRepository.findByWorker(worker, pageable);
    }

    public Page<Booking> getBookingsByJob(Long jobId, Pageable pageable) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return bookingRepository.findByJob(job, pageable);
    }

    public Page<Booking> getBookingsByEmployer(Long employerId, Pageable pageable) {
        return bookingRepository.findByEmployerId(employerId, pageable);
    }

    public Page<Booking> getBookingsByWorkerAndStatus(Long workerId, String status, Pageable pageable) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status);
            return bookingRepository.findByWorkerIdAndStatus(workerId, bookingStatus, pageable);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid booking status");
        }
    }

    public Page<Booking> getBookingsByEmployerAndStatus(Long employerId, String status, Pageable pageable) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status);
            return bookingRepository.findByEmployerIdAndStatus(employerId, bookingStatus, pageable);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid booking status");
        }
    }

    public Page<Booking> getBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return bookingRepository.findByDateRange(startDate, endDate, pageable);
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public Booking createBooking(BookingDto bookingDto) {
        // Get job and worker
        Job job = jobRepository.findById(bookingDto.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        WorkerProfile worker = workerProfileRepository.findById(bookingDto.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        // Validate job is still open
        if (job.getStatus() != Job.JobStatus.OPEN) {
            throw new RuntimeException("Job is not open for booking");
        }

        Booking booking = new Booking();
        booking.setJob(job);
        booking.setWorker(worker);
        booking.setStartTime(bookingDto.getStartTime());
        booking.setEndTime(bookingDto.getEndTime());
        booking.setHourlyRate(bookingDto.getHourlyRate() != null ? bookingDto.getHourlyRate() : job.getHourlyRate());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);

        // After creating booking, update job status if it's now filled
        Booking savedBooking = bookingRepository.save(booking);
        
        // If the job has a maximum number of applicants and we've reached it, mark as filled
        if (job.getMaxApplicants() != null) {
            long bookingCount = bookingRepository.findByJob(job, Pageable.unpaged()).getTotalElements();
            if (bookingCount >= job.getMaxApplicants()) {
                job.setStatus(Job.JobStatus.FILLED);
                jobRepository.save(job);
            }
        }

        return savedBooking;
    }

    @Transactional
    public Booking updateBooking(Long id, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Update fields
        if (bookingDto.getStartTime() != null) {
            booking.setStartTime(bookingDto.getStartTime());
        }
        
        if (bookingDto.getEndTime() != null) {
            booking.setEndTime(bookingDto.getEndTime());
        }
        
        if (bookingDto.getHourlyRate() != null) {
            booking.setHourlyRate(bookingDto.getHourlyRate());
        }
        
        if (bookingDto.getWorkerNotes() != null) {
            booking.setWorkerNotes(bookingDto.getWorkerNotes());
        }
        
        if (bookingDto.getEmployerNotes() != null) {
            booking.setEmployerNotes(bookingDto.getEmployerNotes());
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        try {
            booking.setStatus(Booking.BookingStatus.valueOf(status));
            return bookingRepository.save(booking);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid booking status");
        }
    }

    @Transactional
    public Booking cancelBooking(Long id, String reason, String cancelledBy) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledBy(cancelledBy);
        booking.setCancelledAt(LocalDateTime.now());

        // If this was the only booking for the job, set job back to OPEN
        Job job = booking.getJob();
        long activeBookingsCount = bookingRepository.findByJob(job, Pageable.unpaged())
                .stream()
                .filter(b -> b.getStatus() != Booking.BookingStatus.CANCELLED)
                .count();
        
        if (activeBookingsCount == 0 && job.getStatus() == Job.JobStatus.FILLED) {
            job.setStatus(Job.JobStatus.OPEN);
            jobRepository.save(job);
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkIn(Long id, Double latitude, Double longitude) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is not in a confirmed status");
        }

        booking.setStatus(Booking.BookingStatus.CHECKED_IN);
        booking.setActualStartTime(LocalDateTime.now());
        booking.setCheckinLatitude(latitude);
        booking.setCheckinLongitude(longitude);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkOut(Long id, Double latitude, Double longitude, String notes) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.CHECKED_IN) {
            throw new RuntimeException("Booking is not in a checked-in status");
        }

        LocalDateTime checkoutTime = LocalDateTime.now();
        booking.setStatus(Booking.BookingStatus.CHECKED_OUT);
        booking.setActualEndTime(checkoutTime);
        booking.setCheckoutLatitude(latitude);
        booking.setCheckoutLongitude(longitude);
        
        if (notes != null) {
            booking.setWorkerNotes(notes);
        }

        // Calculate hours worked and total amount
        if (booking.getActualStartTime() != null) {
            Duration duration = Duration.between(booking.getActualStartTime(), checkoutTime);
            double hoursWorked = duration.toMinutes() / 60.0;
            booking.setHoursWorked(hoursWorked);
            booking.setTotalAmount(hoursWorked * booking.getHourlyRate());
        }

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.BookingStatus.CHECKED_OUT) {
            throw new RuntimeException("Booking must be checked out before completing");
        }

        booking.setStatus(Booking.BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }
}