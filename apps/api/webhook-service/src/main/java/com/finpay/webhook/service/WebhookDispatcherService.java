package com.finpay.webhook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finpay.common.entities.Merchant;
import com.finpay.common.entities.WebhookEvent;
import com.finpay.common.events.PaymentEvent;
import com.finpay.common.repositories.MerchantRepository;
import com.finpay.common.repositories.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookDispatcherService {

    private final MerchantRepository merchantRepository;
    private final WebhookEventRepository webhookEventRepository;
    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    public void dispatch(PaymentEvent event) {
        Merchant merchant = merchantRepository.findById(event.getMerchantId())
                .orElse(null);

        if (merchant == null) {
            log.warn("Merchant {} not found. Cannot dispatch webhook for tx={}", event.getMerchantId(),
                    event.getTransactionId());
            return;
        }

        if (merchant.getWebhookUrl() == null || merchant.getWebhookUrl().isBlank()) {
            log.info("Merchant {} has no webhook URL. Skipping dispatch for tx={}", merchant.getId(),
                    event.getTransactionId());
            return;
        }

        String eventId = UUID.randomUUID().toString();
        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", e.getMessage());
            return;
        }

        // Generate HMAC signature
        String signature = generateHmacSha256(payload, merchant.getApiKey());

        log.info("Dispatching webhook to {} [eventId={}]", merchant.getWebhookUrl(), eventId);

        String status = "FAILED";
        try {
            restClient.post()
                    .uri(merchant.getWebhookUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("FinPay-Signature", signature)
                    .header("FinPay-Event-Id", eventId)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            status = "DELIVERED";
            log.info("Webhook delivered successfully [eventId={}]", eventId);
        } catch (Exception e) {
            log.error("Webhook delivery failed [eventId={}]: {}", eventId, e.getMessage());
        }

        // Persist fact
        WebhookEvent webhookEvent = WebhookEvent.builder()
                .merchant(merchant)
                .eventType("payment." + event.getStatus().toLowerCase())
                .payload(payload)
                .signature(signature)
                .status(status)
                .attemptCount(1)
                .deliveredAt("DELIVERED".equals(status) ? OffsetDateTime.now() : null)
                .build();
        webhookEventRepository.save(webhookEvent);
    }

    private String generateHmacSha256(String payload, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] raw = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(raw);
        } catch (Exception e) {
            log.error("Failed to generate HMAC signature: {}", e.getMessage());
            return "";
        }
    }
}
