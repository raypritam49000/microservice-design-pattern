package com.sample.gateway_service.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    @Autowired
    private RouterValidator routerValidator;
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.prefix}")
    public String TOKEN_PREFIX;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request) || this.isPrefixMissing(request))
                return this.onError(exchange, "Authorization header is missing in request");

            final String token = this.getAuthHeader(request);

            if (jwtUtil.isInvalid(token))
                return this.onError(exchange, "Authorization header is invalid");

            this.populateRequestWithHeaders(exchange, token);
        }
        return chain.filter(exchange.mutate().request(request).build());
    }


    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        var header = request.getHeaders().getOrEmpty("Authorization").get(0);
        return header.replace(TOKEN_PREFIX, "").trim();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private boolean isPrefixMissing(ServerHttpRequest request) {
        var header = request.getHeaders().getFirst("Authorization");
        assert header != null;
        return !header.startsWith(TOKEN_PREFIX);
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        System.out.println("------- Claims "+claims);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .header("email", String.valueOf(claims.get("email")))
                .build();
    }
}
