package com.shipping.app.service.impl;

import com.google.gson.Gson;
import com.shipping.app.dto.enums.OrderStatus;
import com.shipping.app.entity.OrderEvent;
import com.shipping.app.repository.OrderEventRepository;
import com.shipping.app.service.ShippingEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShippingEventServiceImpl implements ShippingEventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingEventServiceImpl.class);

    @Autowired
    private OrderEventRepository repository;

    @Autowired
    private Gson gson;

    // Kafka listener to consume order events
    @KafkaListener(topics = "order-events", groupId = "shipping-service")
    @Override
    public void consumeOrderEvent(String orderEventStr) {
        LOGGER.info("Received order event: {}", orderEventStr);
        try {
            OrderEvent orderEvent = gson.fromJson(orderEventStr, OrderEvent.class);
            LOGGER.info("Deserialized order event: {}", orderEvent);

            if (orderEvent.getStatus().equals(OrderStatus.CONFIRMED)) {
                LOGGER.info("Order confirmed: {}", orderEvent.getOrderId());
                // Automatically ship after order confirmation
                shipOrder(orderEvent.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("Error processing order event: {}", orderEventStr, e);
        }
    }

    // Ship the order
    @Override
    public void shipOrder(String orderId) {
        LOGGER.info("Shipping order with ID: {}", orderId);
        try {
            OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.SHIPPED, "Order Shipped successfully", LocalDateTime.now());
            repository.save(orderEvent);
            LOGGER.info("Order {} shipped successfully and saved to repository.", orderId);
        } catch (Exception e) {
            LOGGER.error("Error shipping order {}: {}", orderId, e.getMessage());
        }
    }

    // Deliver the order
    @Override
    public void deliverOrder(String orderId) {
        LOGGER.info("Delivering order with ID: {}", orderId);
        try {
            OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.DELIVERED, "Order delivered successfully", LocalDateTime.now());
            repository.save(orderEvent);
            LOGGER.info("Order {} delivered successfully and saved to repository.", orderId);
        } catch (Exception e) {
            LOGGER.error("Error delivering order {}: {}", orderId, e.getMessage());
        }
    }

    private void saveAndPublishShippingEvent(Object event) {
        // Implement your logic here
    }
}
