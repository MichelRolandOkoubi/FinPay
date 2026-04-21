package com.finpay.payment.service;

import com.finpay.common.entities.Merchant;
import com.finpay.common.entities.Transaction;
import com.finpay.common.repositories.MerchantRepository;
import com.finpay.common.repositories.TransactionRepository;
import com.finpay.payment.dto.CreatePaymentRequest;
import com.finpay.payment.dto.PaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;
    private final StripeService stripeService;
    private final PaymentEventPublisher paymentEventPublisher;

    /**
     * Creates a new payment. Implements idempotency via the idempotencyKey field:
     * if a transaction with that key already exists, the existing result is
     * returned.
     */
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        // 1. Idempotency check
        Optional<Transaction> existing = transactionRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existing.isPresent()) {
            log.info("Idempotent hit for key={}", request.getIdempotencyKey());
            return toResponse(existing.get());
        }

        // 2. Resolve merchant
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found: " + request.getMerchantId()));

        // 3. Create transaction in PENDING state
        Transaction tx = Transaction.builder()
                .merchant(merchant)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .method(request.getMethod())
                .status("PENDING")
                .idempotencyKey(request.getIdempotencyKey())
                .build();
        tx = transactionRepository.save(tx);

        // 4. Call Stripe API
        try {
            PaymentIntent intent = stripeService.createPaymentIntent(
                    request.getAmount(),
                    request.getCurrency(),
                    request.getDescription(),
                    request.getIdempotencyKey());

            tx.setStripePaymentIntentId(intent.getId());
            tx.setStatus(mapStripeStatus(intent.getStatus()));
            tx = transactionRepository.save(tx);

            log.info("Payment created: txId={}, stripeId={}, status={}",
                    tx.getId(), intent.getId(), tx.getStatus());

            publishEvent(tx);

        } catch (StripeException e) {
            log.error("Stripe API error for tx={}: {}", tx.getId(), e.getMessage());
            tx.setStatus("FAILED");
            tx.setMetadata("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
            tx = transactionRepository.save(tx);

            publishEvent(tx);
        }

        return toResponse(tx);
    }

    private void publishEvent(Transaction tx) {
        com.finpay.common.events.PaymentEvent event = com.finpay.common.events.PaymentEvent.builder()
                .transactionId(tx.getId())
                .merchantId(tx.getMerchant().getId())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .status(tx.getStatus())
                .build();
        paymentEventPublisher.publishEvent(event);
    }

    /**
     * Retrieves a payment by its transaction ID.
     */
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(UUID transactionId) {
        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
        return toResponse(tx);
    }

    private PaymentResponse toResponse(Transaction tx) {
        return PaymentResponse.builder()
                .id(tx.getId())
                .amount(tx.getAmount())
                .currency(tx.getCurrency())
                .method(tx.getMethod())
                .status(tx.getStatus())
                .stripePaymentIntentId(tx.getStripePaymentIntentId())
                .idempotencyKey(tx.getIdempotencyKey())
                .createdAt(tx.getCreatedAt())
                .build();
    }

    private String mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus) {
            case "succeeded" -> "SUCCEEDED";
            case "requires_payment_method", "requires_confirmation",
                    "requires_action", "processing" ->
                "PENDING";
            case "canceled" -> "FAILED";
            default -> "PENDING";
        };
    }
}
