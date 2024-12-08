package com.order.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.order.app.events.OrderEvent;
import com.order.app.events.PaymentEvent;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventHandlerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventHandlerService.class);
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void handleOrderEvent(String orderEvent) {
        LOGGER.info("OrderEventHandlerService handleOrderEvent received event : {}", orderEvent);
        OrderEvent event = GSON.fromJson(orderEvent, OrderEvent.class);
        if ("STARTED".equals(event.getStatus())) {
            // Business logic for starting order
            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setOrderId(event.getOrderId());
            paymentEvent.setStatus("INITIATE_PAYMENT");
            LOGGER.info("OrderEventHandlerService handleOrderEvent send PaymentEvent : {}", paymentEvent);
            kafkaTemplate.send("payment-event", event.getOrderId(), GSON.toJson(paymentEvent));
        } else if ("FAILED".equals(event.getStatus())) {
            // Handle rollback logic
        } else if ("COMPLETED".equals(event.getStatus())) {
            // Complete the order
        }
    }
}
