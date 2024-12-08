package com.product.app.service.impl;

import com.product.app.dto.ProductEvent;
import com.product.app.entity.Product;
import com.product.app.repository.ProductRepository;
import com.product.app.service.ProductQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductQueryServiceImpl implements ProductQueryService {

    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> getProducts() {
        return repository.findAll();
    }

    @KafkaListener(topics = "product-event-topic", groupId = "product-event-group")
    public void processProductEvents(ProductEvent productEvent) {
        log.info("@@@ ProductEvent productEvent : {} ",productEvent);
        Product product = productEvent.getProduct();
        if (productEvent.getEventType().equals("CreateProduct")) {
            repository.save(product);
        }
        if (productEvent.getEventType().equals("UpdateProduct")) {
            Product existingProduct = repository.findById(product.getId()).get();
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            repository.save(existingProduct);
        }
    }
}
