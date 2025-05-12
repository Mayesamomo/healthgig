package com.healthgig.platform.modules.payments.controller;

import com.healthgig.platform.modules.payments.dto.PaymentIntentDto;
import com.healthgig.platform.modules.payments.dto.PaymentIntentResponse;
import com.healthgig.platform.modules.payments.model.Payment;
import com.healthgig.platform.modules.payments.service.PaymentService;
import com.healthgig.platform.modules.users.model.User;
import com.healthgig.platform.modules.users.service.UserService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing APIs")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payments", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Payment>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(paymentService.getAllPayments(pageable));
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER', 'WORKER')")
    @Operation(summary = "Get payments by booking", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Payment>> getPaymentsByBooking(
            @PathVariable Long bookingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(paymentService.getPaymentsByBooking(bookingId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYER', 'WORKER')")
    @Operation(summary = "Get payment by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create-payment-intent")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Create payment intent", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(
            @Valid @RequestBody PaymentIntentDto paymentIntentDto) {
        try {
            return ResponseEntity.ok(paymentService.createPaymentIntent(paymentIntentDto));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/confirm/{paymentIntentId}")
    @Operation(summary = "Confirm payment", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> confirmPayment(@PathVariable String paymentIntentId) {
        try {
            return ResponseEntity.ok(paymentService.confirmPayment(paymentIntentId));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/payout/booking/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create worker payout", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> createWorkerPayout(
            @PathVariable Long bookingId,
            @RequestParam Double amount) {
        
        return ResponseEntity.ok(paymentService.createWorkerPayout(bookingId, amount));
    }

    @PostMapping("/payout/confirm/{payoutId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Confirm worker payout", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> confirmWorkerPayout(@PathVariable Long payoutId) {
        return ResponseEntity.ok(paymentService.confirmWorkerPayout(payoutId));
    }

    @PostMapping("/refund/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Refund payment", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> refundPayment(
            @PathVariable Long paymentId,
            @RequestParam Double amount,
            @RequestParam(required = false) String reason) {
        
        try {
            return ResponseEntity.ok(paymentService.refundPayment(paymentId, amount, reason));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Webhook handler for Stripe events
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        // In a real implementation, you would verify the signature and process the event
        // For example:
        // try {
        //     Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        //     // Process the event based on its type
        //     if ("payment_intent.succeeded".equals(event.getType())) {
        //         PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
        //         paymentService.confirmPayment(paymentIntent.getId());
        //     }
        // } catch (SignatureVerificationException e) {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        // }
        
        return ResponseEntity.ok("");
    }
}