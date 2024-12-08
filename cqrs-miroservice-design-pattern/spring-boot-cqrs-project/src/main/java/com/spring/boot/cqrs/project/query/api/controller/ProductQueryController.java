package com.spring.boot.cqrs.project.query.api.controller;

import com.spring.boot.cqrs.project.command.api.model.ProductRestModel;
import com.spring.boot.cqrs.project.query.api.queries.GetProductByIdQuery;
import com.spring.boot.cqrs.project.query.api.queries.GetProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProductQueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductQueryController.class);
    private final QueryGateway queryGateway;

    public ProductQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<ProductRestModel> getAllProducts() {
        LOGGER.info("Handling request to get all products.");
        GetProductsQuery getProductsQuery = new GetProductsQuery();
        List<ProductRestModel> productRestModels = queryGateway.query(getProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
        LOGGER.info("Retrieved {} products.", productRestModels.size());
        return productRestModels;
    }

    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductRestModel> getProductById(@PathVariable String productId) {
        LOGGER.info("Handling request to get product with ID: {}", productId);
        try {
            GetProductByIdQuery query = new GetProductByIdQuery(productId);
            ProductRestModel response = queryGateway.query(query, ProductRestModel.class).join();
            if (Objects.nonNull(response)) {
                LOGGER.info("Product found: {}", response);
                return ResponseEntity.ok(response);
            } else {
                LOGGER.warn("Product with ID {} not found.", productId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            LOGGER.error("Error occurred while retrieving product with ID {}: {}", productId, ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
