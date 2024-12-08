package com.saga.orchestrator.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Entity
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class SagaInstance {
    @Id
    private String sagaId;
    private String orderId;
    private String currentStep;
    private String status;
}