package com.common.security;

import com.common.jsonwebtoken.AuthTokenDetailsDTO;
import com.common.jsonwebtoken.JsonWebTokenUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JsonWebTokenUtility jwtUtility;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {

            LOGGER.info("Processing authentication for request: {}", request.getRequestURI());
            final String authorizationHeader = request.getHeader("Authorization");
            LOGGER.info("@@@ Inside JwtAuthenticationFilter authorizationHeader : {}", authorizationHeader);

            AuthTokenDetailsDTO authTokenDetailsDTO = null;

            if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                authTokenDetailsDTO = jwtUtility.parseAndValidate(jwt);
            }

            LOGGER.info("@@@ Inside JwtAuthenticationFilter authTokenDetailsDTO : {}", authTokenDetailsDTO);

            if (Objects.nonNull(authTokenDetailsDTO) && ObjectUtils.isEmpty(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails userDetails = new User(authTokenDetailsDTO.getUsername(), "", convertAuthoritiesToGrantedAuthorities(authTokenDetailsDTO.getRoles()));
                LOGGER.info("Valid JWT for username: {}", authTokenDetailsDTO.getUsername());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            LOGGER.error("Failed to authenticate the request: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }


    public Set<GrantedAuthority> convertAuthoritiesToGrantedAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

}
