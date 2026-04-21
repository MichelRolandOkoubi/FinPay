package com.finpay.payment.controller;

import com.finpay.payment.dto.CreatePaymentRequest;
import com.finpay.payment.dto.PaymentResponse;
import com.finpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * POST /v1/payments
     * Creates a new payment transaction via Stripe.
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        HttpStatus status = "PENDING".equals(response.getStatus()) || "SUCCEEDED".equals(response.getStatus())
                ? HttpStatus.CREATED
                : HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity.status(status).body(response);
    }

    /**
     * GET /v1/payments/{id}
     * Retrieves a payment by its transaction UUID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID id) {
        PaymentResponse response = paymentService.getPayment(id);
        return ResponseEntity.ok(response);
    }
}
