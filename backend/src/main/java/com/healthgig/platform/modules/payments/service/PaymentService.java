package com.healthgig.platform.modules.payments.service;

import com.healthgig.platform.modules.bookings.model.Booking;
import com.healthgig.platform.modules.bookings.repository.BookingRepository;
import com.healthgig.platform.modules.payments.dto.PaymentIntentDto;
import com.healthgig.platform.modules.payments.dto.PaymentIntentResponse;
import com.healthgig.platform.modules.payments.model.Payment;
import com.healthgig.platform.modules.payments.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.stripe.param.PaymentIntentCreateParams.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    public Page<Payment> getPaymentsByBooking(Long bookingId, Pageable pageable) {
        return paymentRepository.findByBookingId(bookingId, pageable);
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    @Transactional
    public PaymentIntentResponse createPaymentIntent(PaymentIntentDto paymentIntentDto) throws StripeException {
        // Get the booking
        Booking booking = bookingRepository.findById(paymentIntentDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate booking is ready for payment
        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Booking must be completed before making a payment");
        }

        // Convert the amount to cents (Stripe uses smallest currency unit)
        long amountInCents = Math.round(paymentIntentDto.getAmount() * 100);

        // Calculate platform fee (e.g., 10%)
        long platformFeeInCents = Math.round(amountInCents * 0.1);

        // Create payment intent with Stripe
        Builder paramsBuilder = builder()
                .setCurrency(paymentIntentDto.getCurrency())
                .setAmount(amountInCents)
                .setDescription(paymentIntentDto.getDescription() != null ? 
                        paymentIntentDto.getDescription() : 
                        "Payment for booking #" + booking.getId())
                .putMetadata("bookingId", booking.getId().toString())
                .putMetadata("employerId", booking.getJob().getEmployer().getId().toString())
                .putMetadata("workerId", booking.getWorker().getId().toString())
                .setApplicationFeeAmount(platformFeeInCents)
                .setCaptureMethod(CaptureMethod.AUTOMATIC);

        PaymentIntent paymentIntent = PaymentIntent.create(paramsBuilder.build());

        // Create a payment record in the database
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(paymentIntentDto.getAmount());
        payment.setPlatformFee(platformFeeInCents / 100.0);
        payment.setStripePaymentIntentId(paymentIntent.getId());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentType(Payment.PaymentType.EMPLOYER_PAYMENT);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setDescription(paymentIntentDto.getDescription());

        payment = paymentRepository.save(payment);

        // Return client secret for the frontend to complete the payment
        return PaymentIntentResponse.builder()
                .clientSecret(paymentIntent.getClientSecret())
                .paymentIntentId(paymentIntent.getId())
                .bookingId(booking.getId())
                .status(paymentIntent.getStatus())
                .build();
    }

    @Transactional
    public Payment confirmPayment(String paymentIntentId) throws StripeException {
        // Retrieve the payment intent from Stripe
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        // Get the payment record from the database
        Payment payment = paymentRepository.findByStripePaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Update payment status based on the payment intent status
        if ("succeeded".equals(paymentIntent.getStatus())) {
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setTransactionId(paymentIntent.getLatestCharge());
            payment.setReceiptUrl(paymentIntent.getLatestChargeObject() != null ? 
                    paymentIntent.getLatestChargeObject().getReceiptUrl() : 
                    null);
        } else if ("canceled".equals(paymentIntent.getStatus())) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment createWorkerPayout(Long bookingId, Double amount) {
        // Get the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate that the booking has a completed payment
        boolean hasCompletedPayment = paymentRepository.findByBookingId(bookingId, Pageable.unpaged())
                .getContent()
                .stream()
                .anyMatch(p -> p.getPaymentType() == Payment.PaymentType.EMPLOYER_PAYMENT &&
                               p.getStatus() == Payment.PaymentStatus.COMPLETED);

        if (!hasCompletedPayment) {
            throw new RuntimeException("No completed payment found for this booking");
        }

        // Create a payout record
        Payment payout = new Payment();
        payout.setBooking(booking);
        payout.setAmount(amount);
        payout.setStatus(Payment.PaymentStatus.PENDING);
        payout.setPaymentType(Payment.PaymentType.WORKER_PAYOUT);
        payout.setPaymentDate(LocalDateTime.now());
        payout.setDescription("Payout for booking #" + booking.getId());

        return paymentRepository.save(payout);
    }

    @Transactional
    public Payment confirmWorkerPayout(Long payoutId) {
        Payment payout = paymentRepository.findById(payoutId)
                .orElseThrow(() -> new RuntimeException("Payout not found"));

        if (payout.getPaymentType() != Payment.PaymentType.WORKER_PAYOUT) {
            throw new RuntimeException("Not a worker payout");
        }

        // In a real implementation, this would involve calling the Stripe API
        // to create a transfer to the worker's connected account
        
        // For now, we'll just update the status
        payout.setStatus(Payment.PaymentStatus.COMPLETED);
        payout.setTransactionId("manual-" + System.currentTimeMillis());

        return paymentRepository.save(payout);
    }

    @Transactional
    public Payment refundPayment(Long paymentId, Double amount, String reason) throws StripeException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }

        // In a real implementation, this would call the Stripe API to create a refund
        // For the stripe-java implementation, it would look like:
        // Map<String, Object> params = new HashMap<>();
        // params.put("payment_intent", payment.getStripePaymentIntentId());
        // params.put("amount", Math.round(amount * 100));
        // params.put("reason", reason);
        // Refund refund = Refund.create(params);

        // Create a refund record
        Payment refund = new Payment();
        refund.setBooking(payment.getBooking());
        refund.setAmount(amount);
        refund.setStatus(Payment.PaymentStatus.COMPLETED);
        refund.setPaymentType(Payment.PaymentType.REFUND);
        refund.setPaymentDate(LocalDateTime.now());
        refund.setDescription("Refund for payment #" + payment.getId() + (reason != null ? ": " + reason : ""));

        // Update the original payment status
        if (amount.equals(payment.getAmount())) {
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
        } else {
            payment.setStatus(Payment.PaymentStatus.PARTIALLY_REFUNDED);
        }

        paymentRepository.save(payment);
        return paymentRepository.save(refund);
    }
}