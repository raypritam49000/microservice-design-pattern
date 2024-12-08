package com.user.service.config.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class FeignClientInterceptor implements RequestInterceptor {
    private Logger logger = LoggerFactory.getLogger(FeignClientInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        String token = getBearerTokenFromSecurityContext();
        logger.info("Feign Client interceptor: Token :  {} ", token);
        template.header("Authorization", "Bearer " + token);
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
