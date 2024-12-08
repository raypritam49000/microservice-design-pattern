package com.demo.application.productservice.controller;

import com.demo.application.productservice.dto.ProductResponse;
import com.demo.application.productservice.exception.ResourceNotFoundException;
import com.demo.application.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        log.info("Get all products request received.");
        return productService.getAllProducts();
    }

    @GetMapping(value="/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") String productId) {
        log.info("Get product by product id [{}] request received.", productId);
        try {
            ProductResponse productResponse = productService.getProductById(productId);
            log.info("Get product by product id [{}] request completed.", productId);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
