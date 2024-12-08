package com.saga.orchestrator.app.repository;

import com.saga.orchestrator.app.entity.SagaEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaEventRepository extends JpaRepository<SagaEvent, Long> {
}
