package com.order.app.publisher;

import com.google.gson.Gson;
import com.order.app.entity.OrderEvent;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventKafkaPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventKafkaPublisher.class);

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${order.event.topicName}")
    private String topicName;

    @Autowired
    private Gson gson;

    public void sendOrderEvent(OrderEvent orderEvent) {
        LOGGER.info("Publishing order event to Kafka topic: {}", topicName);
        try {
            String orderEventJson = gson.toJson(orderEvent);
            kafkaTemplate.send(topicName, orderEventJson);
            LOGGER.info("Order event successfully published: {}", orderEventJson);
        } catch (Exception e) {
            LOGGER.error("Error publishing order event to Kafka: {}", e.getMessage());
        }
    }
}
