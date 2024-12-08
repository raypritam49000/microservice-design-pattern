package com.api.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/auth/**", "/howdy/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))

                .route("order-service", r -> r.path("/orders/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://order-service"))

                .route("course-service", r -> r.path("/course/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://course-service"))
                .build();
    }

}