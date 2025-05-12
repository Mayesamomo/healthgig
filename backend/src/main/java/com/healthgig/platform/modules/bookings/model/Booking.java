package com.healthgig.platform.modules.bookings.model;

import com.healthgig.platform.modules.jobs.model.Job;
import com.healthgig.platform.modules.users.model.WorkerProfile;
import com.healthgig.platform.shared.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Booking entity representing a confirmed job assignment for a worker.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private WorkerProfile worker;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Column(name = "hours_worked")
    private Double hoursWorked;

    @Column(name = "hourly_rate", nullable = false)
    private Double hourlyRate;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "checkin_latitude")
    private Double checkinLatitude;

    @Column(name = "checkin_longitude")
    private Double checkinLongitude;

    @Column(name = "checkout_latitude")
    private Double checkoutLatitude;

    @Column(name = "checkout_longitude")
    private Double checkoutLongitude;

    @Column(name = "worker_notes")
    private String workerNotes;

    @Column(name = "employer_notes")
    private String employerNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancelled_by")
    private String cancelledBy;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    public enum BookingStatus {
        CONFIRMED,
        CHECKED_IN,
        CHECKED_OUT,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }
}