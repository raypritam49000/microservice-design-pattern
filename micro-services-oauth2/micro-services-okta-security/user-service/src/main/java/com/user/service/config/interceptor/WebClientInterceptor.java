package com.user.service.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
public class WebClientInterceptor implements ExchangeFilterFunction {
    private Logger logger = LoggerFactory.getLogger(WebClientInterceptor.class);

    @Autowired
    private OAuth2AuthorizedClientManager manager;

    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction nextFilter) {
        String token = manager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("my-internal-client").principal("internal").build()).getAccessToken().getTokenValue();
        logger.info("Web Client interceptor: Token :  {} ", token);
        ClientRequest modifiedRequest = ClientRequest.from(clientRequest).header("Authorization", "Bearer " + token).build();
        return nextFilter.exchange(modifiedRequest);
    }
}