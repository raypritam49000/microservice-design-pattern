package com.user.service.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
public class WebClientInterceptor implements ExchangeFilterFunction {
    private Logger logger = LoggerFactory.getLogger(WebClientInterceptor.class);


    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction nextFilter) {
        String token = getBearerTokenFromSecurityContext();
        logger.info("Web Client interceptor Token :  {} ", token);
        ClientRequest modifiedRequest = ClientRequest.from(clientRequest).header("Authorization", "Bearer " + token).build();
        return nextFilter.exchange(modifiedRequest);
    }

    private String getBearerTokenFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof OAuth2Token) {
            OAuth2Token oAuth2Token = (OAuth2Token) authentication.getCredentials();
            return oAuth2Token.getTokenValue();
        }
        return null;
    }
}