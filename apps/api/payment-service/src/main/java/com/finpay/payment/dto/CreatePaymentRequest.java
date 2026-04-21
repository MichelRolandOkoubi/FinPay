package com.finpay.payment.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {
    private Long amount; // amount in cents
    private String currency; // ISO 4217 (EUR, USD, GBP)
    private String method; // CARD | SEPA | SWIFT | APPLE_PAY | GOOGLE_PAY
    private UUID merchantId;
    private String idempotencyKey;
    private String description;
    private String customerEmail;
}
