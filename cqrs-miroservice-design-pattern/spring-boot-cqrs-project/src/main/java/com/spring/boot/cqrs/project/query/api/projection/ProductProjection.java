package com.spring.boot.cqrs.project.query.api.projection;

import com.spring.boot.cqrs.project.command.api.data.Product;
import com.spring.boot.cqrs.project.command.api.data.ProductRepository;
import com.spring.boot.cqrs.project.command.api.model.ProductRestModel;
import com.spring.boot.cqrs.project.query.api.exceptions.ResourceNotFoundException;
import com.spring.boot.cqrs.project.query.api.queries.GetProductByIdQuery;
import com.spring.boot.cqrs.project.query.api.queries.GetProductsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public record ProductProjection(ProductRepository productRepository) {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductProjection.class);

    @QueryHandler
    public List<ProductRestModel> handle(GetProductsQuery getProductsQuery) {
        LOGGER.info("Handling request to retrieve all products.");
        List<Product> products = productRepository.findAll();
        List<ProductRestModel> productRestModels = products.stream()
                .map(product -> ProductRestModel.builder()
                        .productId(product.getProductId())
                        .quantity(product.getQuantity())
                        .price(product.getPrice())
                        .name(product.getName())
                        .build())
                .collect(Collectors.toList());
        LOGGER.info("Retrieved {} products.", productRestModels.size());
        return productRestModels;
    }

    @QueryHandler
    public ProductRestModel handle(GetProductByIdQuery query) {
        LOGGER.info("Handling request to retrieve product with ID: {}", query.getProductId());
        Product productEntity = productRepository.findById(query.getProductId())
                .orElseThrow(() -> {
                    LOGGER.error("Product with ID {} not found.", query.getProductId());
                    return new ResourceNotFoundException("Product not found for ID: " + query.getProductId());
                });
        ProductRestModel response = new ProductRestModel();
        response.setProductId(productEntity.getProductId());
        response.setName(productEntity.getName());
        response.setPrice(productEntity.getPrice());
        response.setQuantity(productEntity.getQuantity());
        LOGGER.info("Product found: {}", response);
        return response;
    }

}
