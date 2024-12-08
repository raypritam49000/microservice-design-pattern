package com.vinsguru.saga.config;

import com.vinsguru.dto.OrchestratorRequestDTO;
import com.vinsguru.dto.OrchestratorResponseDTO;
import com.vinsguru.saga.service.OrchestratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class OrchestratorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrchestratorConfig.class);

    @Autowired
    private OrchestratorService orchestratorService;

    @Bean
    public Function<Flux<OrchestratorRequestDTO>, Flux<OrchestratorResponseDTO>> processor() {
        return flux -> flux.flatMap(dto -> this.orchestratorService.orderProduct(dto))
                .doOnNext(dto -> System.out.println("@@@ Call Processor Status : " + dto.getStatus()));
    }

}
