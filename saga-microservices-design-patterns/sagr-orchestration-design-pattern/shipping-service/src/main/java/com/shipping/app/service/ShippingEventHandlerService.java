package com.shipping.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shipping.app.events.PaymentEvent;
import com.shipping.app.events.ShippingEvent;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShippingEventHandlerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingEventHandlerService.class);
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;


    @KafkaListener(topics = "shipping-event", groupId = "shipping-group")
    public void handleShippingEvent(String shippingEvent) {
        LOGGER.info("ShippingEventHandlerService handleShipping received shipping event : {}", shippingEvent);

        ShippingEvent event = GSON.fromJson(shippingEvent, ShippingEvent.class);
        LOGGER.info("ShippingEventHandlerService handleShipping received shipping event : {}", event);

        if ("START_SHIPPING".equals(event.getStatus())) {

            ShippingEvent shippingSuccessEvent = new ShippingEvent();
            shippingSuccessEvent.setOrderId(event.getOrderId());
            shippingSuccessEvent.setStatus("SUCCESS");

            LOGGER.info("ShippingEventHandlerService handleShipping send shipping event : {}", event);

            kafkaTemplate.send("shipping-events", event.getOrderId(), GSON.toJson(shippingSuccessEvent));
        }
    }
}
