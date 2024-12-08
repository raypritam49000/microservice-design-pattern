package com.spring.boot.cqrs.project.command.api.controller;

import com.spring.boot.cqrs.project.command.api.commands.CreateProductCommand;
import com.spring.boot.cqrs.project.command.api.commands.DeleteProductCommand;
import com.spring.boot.cqrs.project.command.api.commands.UpdateProductCommand;
import com.spring.boot.cqrs.project.command.api.model.ProductRestModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCommandController.class);
    private final CommandGateway commandGateway;

    public ProductCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String addProduct(@RequestBody ProductRestModel productRestModel) {
        LOGGER.info("Received request to add a new product : {}", productRestModel);
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .name(productRestModel.getName())
                .price(productRestModel.getPrice())
                .quantity(productRestModel.getQuantity())
                .build();
        LOGGER.debug("Sending CreateProductCommand : {}", createProductCommand);
        String result = commandGateway.sendAndWait(createProductCommand);
        LOGGER.info("Product created successfully with ID : {}", createProductCommand.getProductId());
        return result;
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") String productId) {
        LOGGER.info("Received request to delete product with ID : {}", productId);
        DeleteProductCommand deleteProductCommand = DeleteProductCommand.builder().productId(productId).build();
        LOGGER.debug("Sending DeleteProductCommand : {}", deleteProductCommand);
        commandGateway.sendAndWait(deleteProductCommand);
        LOGGER.info("Product deleted successfully with ID : {}", productId);
        return "Product with ID " + productId + " deleted successfully.";
    }

    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable("productId") String productId, @RequestBody ProductRestModel model) {
        LOGGER.info("Received request to update product with ID: {}", productId);
        LOGGER.info("Request details - Name: {}, Price: {}, Quantity: {}", model.getName(), model.getPrice(), model.getQuantity());
        UpdateProductCommand command = UpdateProductCommand.builder()
                .productId(productId)
                .name(model.getName())
                .price(model.getPrice())
                .quantity(model.getQuantity())
                .build();
        LOGGER.debug("Sending UpdateProductCommand : {}", command);
        String result = commandGateway.sendAndWait(command);
        LOGGER.info("Product updated successfully. Command result: {}", result);
        return "Product with ID " + productId + " updated successfully.";
    }
}
