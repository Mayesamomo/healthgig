package com.healthgig.platform.modules.bookings.controller;

import com.healthgig.platform.modules.bookings.dto.BookingDto;
import com.healthgig.platform.modules.bookings.model.Booking;
import com.healthgig.platform.modules.bookings.service.BookingService;
import com.healthgig.platform.modules.users.model.User;
import com.healthgig.platform.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management APIs")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all bookings", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Booking>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        return ResponseEntity.ok(bookingService.getAllBookings(pageable));
    }

    @GetMapping("/worker/{workerId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#workerId)")
    @Operation(summary = "Get bookings by worker", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Booking>> getBookingsByWorker(
            @PathVariable Long workerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(bookingService.getBookingsByWorkerAndStatus(workerId, status, pageable));
        } else {
            return ResponseEntity.ok(bookingService.getBookingsByWorker(workerId, pageable));
        }
    }

    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isEmployer(#employerId)")
    @Operation(summary = "Get bookings by employer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Booking>> getBookingsByEmployer(
            @PathVariable Long employerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(bookingService.getBookingsByEmployerAndStatus(employerId, status, pageable));
        } else {
            return ResponseEntity.ok(bookingService.getBookingsByEmployer(employerId, pageable));
        }
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER', 'WORKER')")
    @Operation(summary = "Get bookings by job", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Booking>> getBookingsByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookingService.getBookingsByJob(jobId, pageable));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get bookings by date range", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Booking>> getBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookingService.getBookingsByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('WORKER')")
    @Operation(summary = "Get current worker's bookings", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Booking>> getCurrentWorkerBookings(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getRole() == User.UserRole.WORKER) {
                // Get the worker profile id
                return userService.getWorkerProfile(user.getId())
                        .map(profile -> {
                            Pageable pageable = PageRequest.of(page, size);
                            
                            if (status != null && !status.isEmpty()) {
                                return ResponseEntity.ok(bookingService.getBookingsByWorkerAndStatus(
                                        profile.getId(), status, pageable));
                            } else {
                                return ResponseEntity.ok(bookingService.getBookingsByWorker(
                                        profile.getId(), pageable));
                            }
                        })
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/my-employer-bookings")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Get current employer's bookings", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Booking>> getCurrentEmployerBookings(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getRole() == User.UserRole.EMPLOYER) {
                // Get the employer profile id
                return userService.getEmployerProfile(user.getId())
                        .map(profile -> {
                            Pageable pageable = PageRequest.of(page, size);
                            
                            if (status != null && !status.isEmpty()) {
                                return ResponseEntity.ok(bookingService.getBookingsByEmployerAndStatus(
                                        profile.getId(), status, pageable));
                            } else {
                                return ResponseEntity.ok(bookingService.getBookingsByEmployer(
                                        profile.getId(), pageable));
                            }
                        })
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER', 'WORKER')")
    @Operation(summary = "Get booking by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('WORKER')")
    @Operation(summary = "Create a booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.getUserByEmail(authentication.getName());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getRole() == User.UserRole.WORKER) {
                // Get the worker profile and set the ID on the booking DTO
                return userService.getWorkerProfile(user.getId())
                        .map(profile -> {
                            bookingDto.setWorkerId(profile.getId());
                            return ResponseEntity.status(HttpStatus.CREATED)
                                    .body(bookingService.createBooking(bookingDto));
                        })
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')")
    @Operation(summary = "Update a booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id, 
            @Valid @RequestBody BookingDto bookingDto) {
        
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER')")
    @Operation(summary = "Update booking status", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER', 'WORKER')")
    @Operation(summary = "Cancel a booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id, 
            @RequestParam(required = false) String reason) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cancelledBy = authentication.getName();
        
        return ResponseEntity.ok(bookingService.cancelBooking(id, reason, cancelledBy));
    }

    @PostMapping("/{id}/check-in")
    @PreAuthorize("hasRole('WORKER')")
    @Operation(summary = "Check in for a booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> checkIn(
            @PathVariable Long id, 
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        
        return ResponseEntity.ok(bookingService.checkIn(id, latitude, longitude));
    }

    @PostMapping("/{id}/check-out")
    @PreAuthorize("hasRole('WORKER')")
    @Operation(summary = "Check out from a booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> checkOut(
            @PathVariable Long id, 
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String notes) {
        
        return ResponseEntity.ok(bookingService.checkOut(id, latitude, longitude, notes));
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Mark a booking as completed", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Booking> completeBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }
}