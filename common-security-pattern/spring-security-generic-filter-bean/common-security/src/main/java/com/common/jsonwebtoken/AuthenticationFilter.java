package com.common.jsonwebtoken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

@Component
public class AuthenticationFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JsonWebTokenUtility jsonWebTokenUtility;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            LOGGER.info("Starting authentication filter for request: {}", request.getRequestURI());
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                LOGGER.info("Authorization header is missing or does not contain Bearer token for request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing JWT token");
                return;
            }

            String token = authorizationHeader.substring(7);
            LOGGER.info("Extracted JWT token: {}", token);

            AuthTokenDetailsDTO authTokenDetailsDTO = jsonWebTokenUtility.parseAndValidate(token);

            if (Objects.isNull(authTokenDetailsDTO)) {
                LOGGER.info("Token is invalid. Authentication details : {}", authTokenDetailsDTO);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed");
                return;
            }

            LOGGER.info("Token is valid. Authentication details : {}", authTokenDetailsDTO);

            request.setAttribute("loggedInUser", authTokenDetailsDTO);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            LOGGER.error("Error occurred during authentication : {}", ex.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed");
            return;
        }
        LOGGER.info("Finished authentication filter for request: {}", request.getRequestURI());
    }
}
