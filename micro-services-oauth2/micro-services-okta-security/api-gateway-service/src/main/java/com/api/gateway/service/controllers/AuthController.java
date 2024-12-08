package com.api.gateway.service.controllers;

import com.api.gateway.service.models.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
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

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    //username : raypritam09081998@gmail.com
    //password : 0003pray
    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(WebSession session, @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client, @AuthenticationPrincipal OidcUser user) {
        logger.info("user email id : {} ", user.getEmail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(user.getEmail());
        authResponse.setSessionId(session.getId());
        authResponse.setAccessToken(client.getAccessToken().getTokenValue());
        authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());
        authResponse.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());
        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        authResponse.setAuthorities(authorities);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
