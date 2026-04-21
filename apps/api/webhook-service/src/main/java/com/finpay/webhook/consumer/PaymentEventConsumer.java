package com.finpay.webhook.consumer;

import com.finpay.common.events.PaymentEvent;
import com.finpay.webhook.service.WebhookDispatcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final WebhookDispatcherService webhookDispatcherService;

    @KafkaListener(topics = "payment.events", groupId = "webhook-dispatchers")
    public void consumePaymentEvent(PaymentEvent event) {
        log.info("Received PaymentEvent from Kafka: {}", event);
        try {
            webhookDispatcherService.dispatch(event);
        } catch (Exception e) {
            log.error("Failed to dispatch webhook for tx={}: {}", event.getTransactionId(), e.getMessage());
            // Depending on architecture, here we could send it to a DLQ (Dead Letter Queue)
        }
    }
}
