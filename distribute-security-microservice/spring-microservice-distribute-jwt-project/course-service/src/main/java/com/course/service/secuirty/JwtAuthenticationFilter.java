package com.course.service.secuirty;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Processing authentication for request: {}", request.getRequestURI());
        try {
            String token = getTokenFromRequest(request);
            if (StringUtils.isNotEmpty(token)) {
                UserDetails userDetails = authDetailsFromToken(token);
                if (jwtUtility.validateToken(token, userDetails)) {
                    Authentication authentication = getAuthenticationFromUserDetails(userDetails, token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to authenticate token", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        LOGGER.info("@@@ Inside JwtAuthenticationFilter AuthorizationHeader : {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Authentication getAuthenticationFromUserDetails(UserDetails userDetails, String token) {
        return new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    private UserDetails authDetailsFromToken(String token) {
        String username = jwtUtility.extractUsername(token);
        LOGGER.info("Extracted JWT for username: {}", username);
        Claims claims = jwtUtility.extractClaims(token);
        List<String> authorities = (List<String>) claims.get("roles");
        authorities = Objects.nonNull(authorities) ? authorities : Collections.emptyList();
        LOGGER.info("Extracted JWT claims: {}", authorities);
        return new User(username, "", authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
    }


}
