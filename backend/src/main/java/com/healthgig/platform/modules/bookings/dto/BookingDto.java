package com.healthgig.platform.modules.bookings.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id;

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;

    private Double hoursWorked;

    @DecimalMin(value = "0.01", message = "Hourly rate must be greater than 0")
    private Double hourlyRate;

    private Double totalAmount;
    private Double checkinLatitude;
    private Double checkinLongitude;
    private Double checkoutLatitude;
    private Double checkoutLongitude;
    private String workerNotes;
    private String employerNotes;
    private String status; // Enum as string
    private String cancellationReason;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
}