package com.cloud.apigateway.controller;


import com.cloud.apigateway.models.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);


    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(WebSession session, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client, @AuthenticationPrincipal OidcUser user) {
        logger.info("user email id : {} ", user.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(user.getEmail());
        authResponse.setAccessToken(client.getAccessToken().getTokenValue());
        authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());
        authResponse.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());
        List<String> authorities = user.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
        authResponse.setAuthorities(authorities);
        authResponse.setSessionId(session.getId());

        logger.info("login api response : {} ", user.getEmail());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}