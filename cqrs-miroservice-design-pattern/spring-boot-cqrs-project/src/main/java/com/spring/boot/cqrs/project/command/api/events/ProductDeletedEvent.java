package com.spring.boot.cqrs.project.command.api.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDeletedEvent {
    private String productId;
}