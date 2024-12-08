package com.saga.orchestrator.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Entity
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class SagaEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String sagaId;
    private String eventType;
    private String eventData;
}
