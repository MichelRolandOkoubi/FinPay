package com.finpay.payment.service;

import com.finpay.common.events.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "payment.events";

    public void publishEvent(PaymentEvent event) {
        log.info("Publishing PaymentEvent to topic {}: {}", TOPIC, event);
        kafkaTemplate.send(TOPIC, event.getTransactionId().toString(), event);
    }
}
