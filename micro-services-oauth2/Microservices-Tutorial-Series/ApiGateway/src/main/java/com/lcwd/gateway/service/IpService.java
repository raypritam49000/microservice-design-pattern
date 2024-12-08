package com.lcwd.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class IpService {

    @Autowired
    private WebClient webClient;


    public Mono<Boolean> isAllowedIp(String clientIp) {
        return webClient.get()
                .uri("http://auth-common-service/allowed-ips/findWhiteListIps/" + clientIp)
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
