package com.api.gateway.security;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class AuthenticationFilterFunctions {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final JwtUtility jwtUtility;

    public AuthenticationFilterFunctions(JwtUtility jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> instrument() {
        return (request, next) -> {
            String authHeader = request.headers().header("Authorization").stream().findFirst().orElse(null);
            LOGGER.info("@@@ AuthenticationFilterFunctions AuthHeader : {}", authHeader);
            if (StringUtils.isNotEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    if (!jwtUtility.isInvalid(token)) {
                        Claims claims = jwtUtility.extractAllClaims(token);
                        ServerRequest modifiedRequest = ServerRequest.from(request)
                                .header("id", String.valueOf(claims.get("id")))
                                .header("role", String.valueOf(claims.get("role")))
                                .header("username", String.valueOf(claims.get("username")))
                                .header("email", String.valueOf(claims.get("email")))
                                .build();
                        return next.handle(modifiedRequest);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error processing JWT token errorMessage : {} ", e.getMessage());
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                }
            }
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        };
    }

}
