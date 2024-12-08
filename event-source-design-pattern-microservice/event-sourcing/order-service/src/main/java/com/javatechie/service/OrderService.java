package com.javatechie.service;

import com.javatechie.controller.OrderController;
import com.javatechie.dto.enums.OrderStatus;
import com.javatechie.dto.request.OrderRequest;
import com.javatechie.dto.response.OrderResponse;
import com.javatechie.entity.OrderEvent;
import com.javatechie.publisher.OrderEventKafkaPublisher;
import com.javatechie.repository.OrderEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderEventRepository repository;

    @Autowired
    private OrderEventKafkaPublisher publisher;


    // Handle order creation
    public OrderResponse placeAnOrder(OrderRequest orderRequest) {
        String orderId = UUID.randomUUID().toString().split("-")[0];
        orderRequest.setOrderId(orderId);
        //do request validation and real business logic
        //save that event and publish kafka messages
        OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.CREATED, "Order created successfully", LocalDateTime.now());
        LOGGER.info("@@@ OrderService placeAnOrder method OrderEvent : {}", orderEvent);
        saveAndPublishEvents(orderEvent);
        return new OrderResponse(orderId, OrderStatus.CREATED);
    }

    // Handle order confirmation
    public OrderResponse confirmOrder(String orderId) {
        OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.CONFIRMED, "Order confirmed successfully", LocalDateTime.now());
        LOGGER.info("@@@ OrderService confirmOrder method OrderEvent : {}", orderEvent);
        saveAndPublishEvents(orderEvent);
        return new OrderResponse(orderId, OrderStatus.CONFIRMED);
    }

    private void saveAndPublishEvents(OrderEvent orderEvent) {
        repository.save(orderEvent);
        publisher.sendOrderEvent(orderEvent);
        LOGGER.info("@@@ OrderService saveAndPublishEvents are successfully");
    }


}
