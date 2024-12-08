package com.spring.boot.cqrs.project.command.api.events;

import com.spring.boot.cqrs.project.command.api.data.Product;
import com.spring.boot.cqrs.project.command.api.data.ProductRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product")
public record ProductEventsHandler(ProductRepository productRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        LOGGER.info("Handling ProductCreatedEvent for productId: {}", event.getProductId());
        Product product = new Product();
        BeanUtils.copyProperties(event, product);
        productRepository.save(product);
        LOGGER.info("ProductCreatedEvent handled successfully for productId: {}", event.getProductId());
        // Simulating an exception for demonstration purposes
       // throw new Exception("Exception Occurred during ProductCreatedEvent handling");
    }

    @EventHandler
    public void on(ProductDeletedEvent event) {
        LOGGER.info("Handling ProductDeletedEvent for productId: {}", event.getProductId());
        productRepository.deleteById(event.getProductId());
        LOGGER.info("ProductDeletedEvent handled successfully for productId: {}", event.getProductId());
    }

    @EventHandler
    public void on(ProductUpdatedEvent event) {
        LOGGER.info("Handling ProductUpdatedEvent for productId: {}", event.getProductId());
        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> {
                    LOGGER.error("Product with id {} not found during ProductUpdatedEvent", event.getProductId());
                    return new IllegalArgumentException("Product not found!");
                });
        product.setName(event.getName());
        product.setPrice(event.getPrice());
        product.setQuantity(event.getQuantity());
        productRepository.save(product);
        LOGGER.info("ProductUpdatedEvent handled successfully for productId: {}", event.getProductId());
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        LOGGER.error("Exception occurred during event handling: {}", exception.getMessage(), exception);
        throw exception;  // rethrow the exception after logging it
    }
}
