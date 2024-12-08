package com.api.gateway.config;

import com.api.gateway.model.AuthTokenModel;
import com.api.gateway.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilterConfig> {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    @Lazy
    private AuthService authService;

    @SuppressWarnings("deprecation")
    @Override
    public GatewayFilter apply(final AuthFilterConfig config) {
        return (exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();

            final boolean authorization = request.getHeaders().containsKey("Authorization");
            log.debug("authorization {}", authorization);

            if (!request.getHeaders().containsKey("apikey"))
                return this.onError(exchange, "No API KEY header", HttpStatus.UNAUTHORIZED);

            final String apiValue = Objects.requireNonNull(request.getHeaders().get("apikey")).getFirst();

            if (StringUtils.isEmpty(apiValue))
                return this.onError(exchange, "Invalid API KEY", HttpStatus.UNAUTHORIZED);

            return authService.getJWTToken(apiValue)
                    .flatMap(authTokenModel -> {
                        log.info("@@@ authTokenModel : {} ",authTokenModel);
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("Authorization", authTokenModel.getType() + " " + authTokenModel.getToken())
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(e -> {
                        log.error("Gateway Auth Error: {}", e.getMessage());
                        return this.onError(exchange, "Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
                    });
        };
    }

    private Mono<Void> onError(final ServerWebExchange exchange, final String err, final HttpStatus httpStatus) {
        log.error("Gateway Auth Error {}", err);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
