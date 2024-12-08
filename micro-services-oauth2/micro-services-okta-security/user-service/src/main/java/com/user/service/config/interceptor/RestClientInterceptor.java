package com.user.service.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestClientInterceptor implements ClientHttpRequestInterceptor {
    private Logger logger = LoggerFactory.getLogger(RestClientInterceptor.class);

    @Autowired
    private OAuth2AuthorizedClientManager manager;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String token = manager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("my-internal-client").principal("internal").build()).getAccessToken().getTokenValue();
        logger.info("Rest Client interceptor: Token :  {} ", token);
        request.getHeaders().add("Authorization", "Bearer " + token);
        return execution.execute(request, body);
    }

}