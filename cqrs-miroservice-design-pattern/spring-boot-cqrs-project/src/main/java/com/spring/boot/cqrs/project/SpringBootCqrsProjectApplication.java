package com.spring.boot.cqrs.project;

import com.spring.boot.cqrs.project.command.api.exception.ProductServiceEventsErrorHandler;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootCqrsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCqrsProjectApplication.class, args);
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerListenerInvocationErrorHandler("product", configuration -> new ProductServiceEventsErrorHandler());
    }

}
