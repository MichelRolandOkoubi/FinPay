package com.finpay.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {
    private UUID transactionId;
    private UUID merchantId;
    private Long amount;
    private String currency;
    private String status;
}
