package com.user.service.security;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Processing authentication for request: {}", request.getRequestURI());
        final String authorizationHeader = request.getHeader("Authorization");
        LOGGER.info("@@@ Inside JwtAuthenticationFilter authorizationHeader : {}", authorizationHeader);
        String username = null;
        String jwt = null;
        List<String> authorities = new ArrayList<String>();

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtility.extractUsername(jwt);
            LOGGER.info("Extracted JWT for username: {}", username);
            Claims claims = jwtUtility.extractClaims(jwt);
            authorities = (List<String>) claims.get("roles");
            LOGGER.info("Extracted JWT for claims: {}", authorities);
        }

        if (StringUtils.isNotEmpty(username) && ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = new User(username, "", convertAuthoritiesToGrantedAuthorities(authorities));
            if (jwtUtility.validateToken(jwt, userDetails)) {
                LOGGER.info("Valid JWT for username: {}", username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    public Set<GrantedAuthority> convertAuthoritiesToGrantedAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

}
