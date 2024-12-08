package com.demo.application.productservice.service;

import com.demo.application.productservice.dto.ProductEvent;
import com.demo.application.productservice.dto.ProductResponse;
import com.demo.application.productservice.exception.ResourceNotFoundException;
import com.demo.application.productservice.model.Product;
import com.demo.application.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("Fetched [{}] products successfully.", products.size());
        return products.stream().map(
                this::fromProductToProductResponse
        ).toList();
    }

    public ProductResponse getProductById(String productId) throws ResourceNotFoundException {
        Product product = productRepository.findByProductId(productId);

        if(Objects.isNull(product)) {
            throw new ResourceNotFoundException(String.format("Product not found with id: %s", productId));
        }
        log.info("Fetched [{}] product successfully.", product.getProductId());
        return fromProductToProductResponse(product);
    }

    private ProductResponse fromProductToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setProductId(product.getProductId());
        productResponse.setName(product.getName());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setDescription(product.getDescription());
        return productResponse;
    }

    @KafkaListener(topics = "product-event", groupId = "product-event-group")
    public void processProductEvents(ProductEvent productEvent) throws ResourceNotFoundException, IllegalAccessException {
        Product product = productEvent.getProduct();
        if(productEvent.getEventType().equals("CreateProduct")) {
            log.info("Create product [{}] received.", product.getProductId());
            productRepository.save(product);
            log.info("Created product [{}] successfully.", product.getProductId());
        } else if (productEvent.getEventType().equals("UpdateProduct")) {
            log.info("Update product [{}] received.", product.getProductId());
            Product existingProduct = productRepository.findByProductId(product.getProductId());

            if(Objects.isNull(existingProduct)) {
                throw new ResourceNotFoundException(String.format("Product not found with id: %s", product.getProductId()));
            }

            ArrayList<String> nullFieldsInProductRequest = getNullFields(product);
            BeanUtils.copyProperties(product, existingProduct, nullFieldsInProductRequest.toArray(String[]::new));
            productRepository.save(existingProduct);
            log.info("Updated product [{}] successfully.", product.getProductId());
        }
    }

    private ArrayList<String> getNullFields(Object object) throws IllegalAccessException {
        ArrayList<String> nullFields = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            if (field.get(object) == null) {
                nullFields.add(field.getName());
            }
        }
        return nullFields;
    }
}
