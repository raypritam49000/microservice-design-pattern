package com.order.app.service.impl;

import com.order.app.dto.enums.OrderStatus;
import com.order.app.dto.request.OrderRequest;
import com.order.app.dto.response.OrderResponse;
import com.order.app.entity.OrderEvent;
import com.order.app.publisher.OrderEventKafkaPublisher;
import com.order.app.repository.OrderEventRepository;
import com.order.app.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderEventRepository repository;

    @Autowired
    private OrderEventKafkaPublisher publisher;

    @Override
    // Handle order creation
    public OrderResponse placeAnOrder(OrderRequest orderRequest) {
        String orderId = UUID.randomUUID().toString().split("-")[0];
        orderRequest.setOrderId(orderId);
        // Log order creation process
        LOGGER.info("Placing order with details: {}", orderRequest);
        //do request validation and real business logic
        //save that event and publish kafka messages
        try {
            OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.CREATED, "Order created successfully", LocalDateTime.now());
            saveAndPublishEvents(orderEvent);
            LOGGER.info("Order created successfully with order ID: {}", orderId);
            return new OrderResponse(orderId, OrderStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error occurred while placing order: {}", e.getMessage(), e);
            throw e; // Re-throw to let higher layers handle it if necessary
        }
    }

    @Override
    // Handle order confirmation
    public OrderResponse confirmOrder(String orderId) {
        LOGGER.info("Confirming order with ID: {}", orderId);
        try {
            OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.CONFIRMED, "Order confirmed successfully", LocalDateTime.now());
            saveAndPublishEvents(orderEvent);
            LOGGER.info("Order confirmed successfully with order ID: {}", orderId);
            return new OrderResponse(orderId, OrderStatus.CONFIRMED);
        } catch (Exception e) {
            LOGGER.error("Error occurred while confirming order with ID {}: {}", orderId, e.getMessage(), e);
            throw e; // Re-throw to let higher layers handle it if necessary
        }
    }

    private void saveAndPublishEvents(OrderEvent orderEvent) {
        try {
            repository.save(orderEvent);
            publisher.sendOrderEvent(orderEvent);
            LOGGER.info("Order event saved and published successfully for order ID: {}", orderEvent.getOrderId());
        } catch (Exception e) {
            LOGGER.error("Error occurred while saving and publishing order event for order ID {}: {}", orderEvent.getOrderId(), e.getMessage(), e);
            throw e; // Re-throw to handle at a higher level if necessary
        }
    }
}
