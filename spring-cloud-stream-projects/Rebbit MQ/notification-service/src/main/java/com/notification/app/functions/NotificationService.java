package com.notification.app.functions;

import com.notification.app.dto.OrderInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class NotificationService {

    @Bean
    public Supplier<String> testing() {
        return () -> "This is Testing";
    }

    @Bean
    public Function<String, String> sayHello() {
        return message -> "Hello How are you ? " + message;
    }

    @Bean
    public Function<OrderInformation, String> orderNotification() {
        return orderInformation -> {
            System.out.println("send notification...");
            sendNotification(orderInformation);
            System.out.println(orderInformation.getEmailId());
            System.out.println(orderInformation.getPrice());
            System.out.println(orderInformation.getUserPhone());
            return orderInformation.getOrderId();
        };
    }

    private void sendNotification(OrderInformation orderInformation) {
    }

}
