package com.healthgig.platform.modules.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentIntentResponse {

    private String clientSecret;
    private String paymentIntentId;
    private Long bookingId;
    private String status;
}