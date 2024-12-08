package com.demo.application.productservice.controller;

import com.demo.application.productservice.dto.ProductRequest;
import com.demo.application.productservice.dto.ProductResponse;
import com.demo.application.productservice.exception.ResourceNotFoundException;
import com.demo.application.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest)  {
        log.info("Product [{}] create request received.", productRequest.getName());
        try {
            productService.createProduct(productRequest);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        log.info("Product [{}] create request completed.", productRequest.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(value="/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable("productId") String productId) {
        log.info("Update product by product d [{}] request received.", productId);
        try {
            ProductResponse productResponse = productService.updateProduct(productRequest, productId);
            log.info("Update product by id [{}] request completed.", productId);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
