package com.api.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class ApiGatewaySecurityConfig {

    @Autowired
    private AuthenticationFilterFunctions filterFunctions;

//    @Bean
//    public RouterFunction<ServerResponse> instrumentRoute() {
//        return RouterFunctions.route()
//                .POST("/auth/**", http("http://localhost:9998"))
//                .GET("/howdy/**", request -> filterFunctions.instrument().filter(request, http("http://localhost:9998")))
//                .GET("/orders/**", request -> filterFunctions.instrument().filter(request, http("http://localhost:9997")))
//                .GET("/course/**", request -> filterFunctions.instrument().filter(request, http("http://localhost:9996")))
//                .build();
//    }


    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return RouterFunctions.route()
                .POST("/auth/**", http()).filter(lb("user-service"))
                .GET("/howdy/**", request -> filterFunctions.instrument().filter(request, http())).filter(lb("user-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return RouterFunctions.route()
                .GET("/orders/**", request -> filterFunctions.instrument().filter(request, http())).filter(lb("order-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> courseServiceRoute() {
        return RouterFunctions.route()
                .GET("/course/**", request -> filterFunctions.instrument().filter(request, http())).filter(lb("course-service"))
                .build();
    }
}