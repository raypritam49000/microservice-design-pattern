package com.spring.boot.cqrs.project.command.api.aggregate;

import com.spring.boot.cqrs.project.command.api.commands.CreateProductCommand;
import com.spring.boot.cqrs.project.command.api.commands.DeleteProductCommand;
import com.spring.boot.cqrs.project.command.api.commands.UpdateProductCommand;
import com.spring.boot.cqrs.project.command.api.events.ProductCreatedEvent;
import com.spring.boot.cqrs.project.command.api.events.ProductDeletedEvent;
import com.spring.boot.cqrs.project.command.api.events.ProductUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductAggregate.class);
    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
        LOGGER.info("Default constructor called for ProductAggregate");
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        LOGGER.info("Handling CreateProductCommand: {}", createProductCommand);
        // Perform validations if necessary
        LOGGER.info("Emit a CreateProductCommand : {}", createProductCommand);
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        LOGGER.info("Applying ProductCreatedEvent: {}", productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        LOGGER.info("Handling ProductCreatedEvent: {}", productCreatedEvent);
        this.productId = productCreatedEvent.getProductId();
        this.name = productCreatedEvent.getName();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
        LOGGER.info("State updated in ProductAggregate: productId={}, name={}, price={}, quantity={}", this.productId, this.name, this.price, this.quantity);
    }

    @CommandHandler
    public void handle(DeleteProductCommand deleteProductCommand) {
        LOGGER.info("Handling DeleteProductCommand : {}", deleteProductCommand);
        LOGGER.info("Emit a ProductDeletedEvent : {}", deleteProductCommand);
        ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(deleteProductCommand.getProductId());
        LOGGER.info("Applying ProductDeletedEvent : {}", productDeletedEvent);
        AggregateLifecycle.apply(productDeletedEvent);
    }

    @EventSourcingHandler
    public void on(ProductDeletedEvent event) {
        LOGGER.info("Handling ProductDeletedEvent : {}", event);
        AggregateLifecycle.markDeleted();
        LOGGER.info("Aggregate marked as deleted.");
        this.name = null;
        this.price = BigDecimal.ZERO;
        this.quantity = 0;
        LOGGER.info("State cleared in ProductAggregate after deletion");
    }

    @CommandHandler
    public void handle(UpdateProductCommand command) {
        LOGGER.info("Handling UpdateProductCommand for productId : {}", command.getProductId());
        LOGGER.info("Updated details - Name: {}, Price: {}, Quantity: {}", command.getName(), command.getPrice(), command.getQuantity());
        ProductUpdatedEvent productUpdatedEvent = new ProductUpdatedEvent(command.getProductId(), command.getName(), command.getPrice(), command.getQuantity());
        LOGGER.info("Applying ProductUpdatedEvent for productId : {}", command.getProductId());
        AggregateLifecycle.apply(productUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductUpdatedEvent event) {
        LOGGER.info("Handling ProductUpdatedEvent for productId: {}", event.getProductId());
        LOGGER.info("Updated product state - Name: {}, Price: {}, Quantity: {}", event.getName(), event.getPrice(), event.getQuantity());
        this.productId = event.getProductId();
        this.name = event.getName();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
    }
}
