package com.saga.orchestrator.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic orderTopic() {
        return new NewTopic("order-events", 1, (short) 1);
    }

    @Bean
    public NewTopic paymentTopic() {
        return new NewTopic("payment-events", 1, (short) 1);
    }

    @Bean
    public NewTopic shippingTopic() {
        return new NewTopic("shipping-events", 1, (short) 1);
    }
}
