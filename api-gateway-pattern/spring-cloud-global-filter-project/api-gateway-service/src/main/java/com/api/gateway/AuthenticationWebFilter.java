package com.api.gateway;

import io.jsonwebtoken.Claims;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class AuthenticationWebFilter implements WebFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationWebFilter.class);

    @Autowired
    private JwtUtility jwtUtil;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> openApiEndpoints = Arrays.asList(
            "/auth/login",
            "/auth/register"
    );

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        try {

            LOGGER.info("Received request for path: {}", request.getURI().getPath());

            // Permit specific URLs without JWT validation
            if (isPermitted(request)) {
                LOGGER.info("Request to {} is permitted without authentication.", request.getURI().getPath());
                return chain.filter(exchange);
            }

            // Check for the Authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                LOGGER.error("Authorization header is missing for request to {}", request.getURI().getPath());
                return this.onError(exchange, "Authorization header is missing");
            }

            String authHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).getFirst();
            if (!authHeader.startsWith("Bearer ")) {
                LOGGER.error("Authorization header is invalid for request to {}", request.getURI().getPath());
                return this.onError(exchange, "Authorization header is invalid");
            }

            String token = authHeader.substring(7);

            LOGGER.info("Extracted JWT Token: {}", token);

            if (jwtUtil.isInvalid(token)) {
                LOGGER.error("Invalid JWT token for request to {}", request.getURI().getPath());
                return this.onError(exchange, "Authorization header is invalid");
            }

            Claims claims = jwtUtil.extractAllClaims(token);
            LOGGER.info("JWT claims extracted: id={}, role={}, username={}, email={}",
                    claims.get("id"), claims.get("role"), claims.get("username"), claims.get("email"));

            // Add claims to request headers to pass them to downstream services
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("id", String.valueOf(claims.get("id")))
                    .header("role", String.valueOf(claims.get("role")))
                    .header("username", String.valueOf(claims.get("username")))
                    .header("email", String.valueOf(claims.get("email")))
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            LOGGER.error("Error processing the JWT token for request to {}", request.getURI().getPath(), e);
            return this.onError(exchange, e.getMessage());
        }
    }

    private boolean isPermitted(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        boolean permitted = openApiEndpoints.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
        if (permitted) {
            LOGGER.info("Path {} is matched with permitted open API endpoints.", path);
        }
        return permitted;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorResponse = new JSONObject(Map.<String, Object>of("status", HttpStatus.FORBIDDEN.name(), "statusCode", HttpStatus.FORBIDDEN.value(), "statusMessage", HttpStatus.FORBIDDEN.getReasonPhrase(), "errorMessage", err)).toString();
        LOGGER.error("Request error: {}", errorResponse);

        byte[] bytes = errorResponse.getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
