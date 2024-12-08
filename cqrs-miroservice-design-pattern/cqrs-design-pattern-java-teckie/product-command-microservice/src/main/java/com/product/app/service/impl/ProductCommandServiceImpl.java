package com.product.app.service.impl;

import com.product.app.dto.ProductEvent;
import com.product.app.entity.Product;
import com.product.app.repository.ProductRepository;
import com.product.app.service.ProductCommandService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private final ProductRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductCommandServiceImpl(ProductRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Product createProduct(ProductEvent productEvent) {
        Product productDO = repository.save(productEvent.getProduct());
        ProductEvent event = new ProductEvent("CreateProduct", productDO);
        kafkaTemplate.send("product-event-topic", event);
        return productDO;
    }

    @Override
    public Product updateProduct(String id, ProductEvent productEvent) {
        Product existingProduct = repository.findById(id).get();
        Product newProduct = productEvent.getProduct();
        existingProduct.setName(newProduct.getName());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setDescription(newProduct.getDescription());
        Product productDO = repository.save(existingProduct);
        ProductEvent event = new ProductEvent("UpdateProduct", productDO);
        kafkaTemplate.send("product-event-topic", event);
        return productDO;
    }
}
