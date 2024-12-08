package com.payment.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.app.events.PaymentEvent;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventHandlerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventHandlerService.class);
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "payment-event", groupId = "payment-group")
    public void handlePaymentEvent(String paymentEvent) {
        LOGGER.info("PaymentEventHandlerService handlePaymentEvent paymentEvent : {}", paymentEvent);
        PaymentEvent event = GSON.fromJson(paymentEvent, PaymentEvent.class);
        if ("INITIATE_PAYMENT".equals(event.getStatus())) {
            // Payment logic
            PaymentEvent paymentSuccessEvent = new PaymentEvent();
            paymentSuccessEvent.setOrderId(event.getOrderId());
            paymentSuccessEvent.setStatus("PAYMENT_COMPLETED");
            LOGGER.info("PaymentEventHandlerService handlePaymentEvent send paymentEvent : {}", paymentSuccessEvent);
            kafkaTemplate.send("payment-events", event.getOrderId(), GSON.toJson(paymentSuccessEvent));
        }
    }
}
