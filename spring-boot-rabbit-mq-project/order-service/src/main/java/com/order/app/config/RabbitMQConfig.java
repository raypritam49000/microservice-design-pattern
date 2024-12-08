package com.order.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "message_exchange";
    public static final String REQUEST_ROUTING_KEY = "request.key";
    public static final String RESPONSE_ROUTING_KEY = "response.key";
    public static final String REQUEST_QUEUE = "request.queue";
    public static final String RESPONSE_QUEUE = "response.queue";

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE, true);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding requestBinding(@Qualifier("requestQueue") Queue requestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding responseBinding(@Qualifier("responseQueue") Queue responseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(responseQueue).to(exchange).with(RESPONSE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}