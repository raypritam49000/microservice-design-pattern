package com.api.gateway.service;

import com.api.gateway.model.AuthTokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<AuthTokenModel> getJWTToken(String apiKey) {
        return webClientBuilder.build()
                .get()
                .uri("http://auth-service/auth/generate")
                .header("apikey", apiKey)
                .retrieve()
                .bodyToMono(AuthTokenModel.class);
    }
}
