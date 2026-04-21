package com.finpay.payment.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
        log.info("Stripe SDK initialized");
    }

    /**
     * Creates a Stripe PaymentIntent for the given amount and currency.
     *
     * @param amountInCents  the amount in the smallest currency unit (e.g. cents)
     * @param currency       ISO 4217 currency code (e.g. "eur", "usd")
     * @param description    optional description for the payment
     * @param idempotencyKey unique key to prevent duplicate charges
     * @return the created PaymentIntent
     * @throws StripeException if Stripe API call fails
     */
    public PaymentIntent createPaymentIntent(Long amountInCents, String currency,
            String description, String idempotencyKey)
            throws StripeException {

        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency.toLowerCase())
                .addPaymentMethodType("card")
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.AUTOMATIC);

        if (description != null && !description.isBlank()) {
            paramsBuilder.setDescription(description);
        }

        PaymentIntentCreateParams params = paramsBuilder.build();

        PaymentIntent intent = PaymentIntent.create(params,
                com.stripe.net.RequestOptions.builder()
                        .setIdempotencyKey(idempotencyKey)
                        .build());

        log.info("Created Stripe PaymentIntent: id={}, status={}", intent.getId(), intent.getStatus());
        return intent;
    }

    /**
     * Retrieves an existing PaymentIntent by its ID.
     */
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }
}
