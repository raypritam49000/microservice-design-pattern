package com.demo.application.productservice.service;

import com.demo.application.productservice.dto.ProductEvent;
import com.demo.application.productservice.dto.ProductRequest;
import com.demo.application.productservice.dto.ProductResponse;
import com.demo.application.productservice.exception.ResourceNotFoundException;
import com.demo.application.productservice.model.Product;
import com.demo.application.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void createProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setProductId(productRequest.getProductId());
        product.setQuantity(productRequest.getQuantity());
        product.setDescription(productRequest.getDescription());

        Product savedProduct = null;
        try {
            savedProduct = productRepository.save(product);
            log.info("Product [{}] persisted - Id: [{}].", productRequest.getName(), savedProduct.getId());
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
        ProductEvent productEvent = new ProductEvent("CreateProduct", savedProduct);
        kafkaTemplate.send("product-event", productEvent);
        log.info("Product [{}] create request completed [{}].", productRequest.getName(), savedProduct.getProductId());
    }

    public ProductResponse updateProduct(ProductRequest productRequest, String productId) throws IllegalAccessException, ResourceNotFoundException {
        log.info("Product update request received [{}].", productId);
        Product product = productRepository.findByProductId(productId);

        if(Objects.isNull(product)) {
            throw new ResourceNotFoundException(String.format("Product not found with product id: %s", productId));
        }

        ArrayList<String> nullFieldsInProductRequest = getNullFields(productRequest);
        BeanUtils.copyProperties(productRequest, product, nullFieldsInProductRequest.toArray(String[]::new));
        Product updatedProduct = productRepository.save(product);
        log.info("Updated product persisted [{}].", productId);

        ProductEvent productEvent = new ProductEvent("UpdateProduct", updatedProduct);
        kafkaTemplate.send("product-event", productEvent);

        log.info("Product [{}] update request completed [{}].", updatedProduct.getName(), updatedProduct.getProductId());
        return fromProductToProductResponse(updatedProduct);
    }

    private ProductResponse fromProductToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setProductId(product.getProductId());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setDescription(product.getDescription());
        return productResponse;
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
