package com.saga.orchestrator.app.controller;


import com.saga.orchestrator.app.service.SagaOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saga")
public class SagaOrchestratorController {

    private final SagaOrchestrator sagaOrchestrator;

    public SagaOrchestratorController(SagaOrchestrator sagaOrchestrator) {
        this.sagaOrchestrator = sagaOrchestrator;
    }

    @PostMapping("/start/{orderId}")
    public ResponseEntity<String> startSaga(@PathVariable("orderId") String orderId) {
        sagaOrchestrator.startSaga(orderId);
        return ResponseEntity.ok("Saga started for order: " + orderId);
    }
}
