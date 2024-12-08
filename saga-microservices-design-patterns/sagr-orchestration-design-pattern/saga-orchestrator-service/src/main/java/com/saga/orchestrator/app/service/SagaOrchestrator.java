package com.saga.orchestrator.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saga.orchestrator.app.entity.SagaEvent;
import com.saga.orchestrator.app.entity.SagaInstance;
import com.saga.orchestrator.app.events.OrderEvent;
import com.saga.orchestrator.app.events.PaymentEvent;
import com.saga.orchestrator.app.events.ShippingEvent;
import com.saga.orchestrator.app.repository.SagaEventRepository;
import com.saga.orchestrator.app.repository.SagaInstanceRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SagaOrchestrator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SagaOrchestrator.class);
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private SagaInstanceRepository sagaInstanceRepository;
    @Autowired
    private SagaEventRepository sagaEventRepository;

    public void startSaga(String orderId) {
        String sagaId = UUID.randomUUID().toString();
        SagaInstance saga = new SagaInstance();
        saga.setSagaId(sagaId);
        saga.setOrderId(orderId);
        saga.setCurrentStep("OrderService");
        saga.setStatus("STARTED");

        sagaInstanceRepository.save(saga);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(orderId);
        orderEvent.setStatus("STARTED");

        saveSagaEvent(sagaId, "OrderEvent", orderEvent);
        kafkaTemplate.send("order-events", orderId, GSON.toJson(orderEvent));
    }

    @KafkaListener(topics = "payment-events", groupId = "saga-group")
    public void handlePaymentEvent(String paymentEvent) {

        PaymentEvent event = GSON.fromJson(paymentEvent, PaymentEvent.class);
        LOGGER.info("SagaOrchestrator handlePaymentEvent Received Payment Event: {}", event);

        SagaInstance saga = sagaInstanceRepository.findByOrderId(event.getOrderId()).orElseThrow();
        LOGGER.info("SagaOrchestrator handlePaymentEvent SagaInstance : {}", GSON.toJson(saga));

        if ("PAYMENT_COMPLETED".equals(event.getStatus())) {
            saga.setCurrentStep("ShippingService");
            saga.setStatus("IN_PROGRESS");
            sagaInstanceRepository.save(saga);

            ShippingEvent shippingEvent = new ShippingEvent();
            shippingEvent.setOrderId(event.getOrderId());
            shippingEvent.setStatus("START_SHIPPING");

            saveSagaEvent(saga.getSagaId(), "ShippingEvent", shippingEvent);

            LOGGER.info("SagaOrchestrator handlePaymentEvent Received send Shipping Event: {}", shippingEvent);
            kafkaTemplate.send("shipping-event", event.getOrderId(), GSON.toJson(shippingEvent));
        } else {
            saga.setStatus("FAILED");
            sagaInstanceRepository.save(saga);
        }
    }


    @KafkaListener(topics = "shipping-events", groupId = "saga-group")
    public void handleShippingEvent(String shippingEvent) {
        ShippingEvent event = GSON.fromJson(shippingEvent, ShippingEvent.class);
        LOGGER.info("SagaOrchestrator handleShippingEvent Received Shipping Event: {}", event);

        SagaInstance saga = sagaInstanceRepository.findByOrderId(event.getOrderId()).orElseThrow();
        LOGGER.info("SagaOrchestrator handleShippingEvent SagaInstance : {}", GSON.toJson(saga));

        if ("SUCCESS".equals(event.getStatus())) {
            saga.setCurrentStep("Completed");
            saga.setStatus("COMPLETED");
        } else {
            saga.setStatus("FAILED");
        }

        sagaInstanceRepository.save(saga);
        saveSagaEvent(saga.getSagaId(), "ShippingEvent", event);
    }

    private void saveSagaEvent(String sagaId, String eventType, Object event) {
        SagaEvent sagaEvent = new SagaEvent();
        sagaEvent.setSagaId(sagaId);
        sagaEvent.setEventType(eventType);
        sagaEvent.setEventData(GSON.toJson(event));
        sagaEventRepository.save(sagaEvent);
    }
}

