package com.client.service.security;

import com.client.service.config.PropertySource;
import com.client.service.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private PropertySource propertySource;

    @Autowired
    private JwtAuthManager jwtAuthManager;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        logger.info("@@@ Inside AuthenticationFilter ::: ");

        try {
            SecurityContextHolder.getContext().setAuthentication(getUser(request));
        } catch (Exception e) {
            final String msg = String.format("Authorization Failed %s", e.getMessage());
            //log.error(msg);
            throw new AuthException(msg, e);
        }
        filterChain.doFilter(request, response);
    }

    private final AuthenticationModel getUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        logger.info("@@@ Inside AuthenticationFilter Authorization : {}", authorizationHeader);
        final AuthenticationModel authenticationModel = new AuthenticationModel();
        authenticationModel.setAuthenticated(false);
        authenticationModel.setCredentials(authorizationHeader);
        return authenticationModel;
    }

}
