package com.finpay.payment.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private UUID id;
    private Long amount;
    private String currency;
    private String method;
    private String status;
    private String stripePaymentIntentId;
    private String idempotencyKey;
    private OffsetDateTime createdAt;
}
