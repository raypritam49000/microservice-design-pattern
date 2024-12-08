package com.course.service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final JwtUtility jwtUtil;

    public AuthenticationFilter(JwtUtility jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        LOGGER.info("Entering AuthenticationFilter");
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            LOGGER.info("Authorization Header: {}", authHeader);

            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
                LOGGER.info("Authorization header is missing or does not start with 'Bearer '");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            final String token = authHeader.substring(7);
            LOGGER.info("Extracted Token: {}", token);

            if (jwtUtil.isInvalid(token)) {
                LOGGER.warn("Token is invalid");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            Claims claims = jwtUtil.extractAllClaims(token);
            LOGGER.info("Claims extracted: {}", claims);
            HttpServletRequest mutatedRequest = new MutatedHttpServletRequest(request, claims);

            // Now you can access custom headers in the mutated request
            String userId = mutatedRequest.getHeader("id");
            String userRole = mutatedRequest.getHeader("role");
            String username = mutatedRequest.getHeader("username");
            String email = mutatedRequest.getHeader("email");

            LOGGER.info("Custom Headers - id: {}, role: {}, username: {}, email: {}", userId, userRole, username, email);

            filterChain.doFilter(mutatedRequest, response);

            LOGGER.info("Exiting AuthenticationFilter");
        } catch (Exception e) {
            LOGGER.error("Error occurred during authentication filter : {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }


}