package com.course.service.secuirty;

import com.course.service.secuirty.jsonwebtoken.JwtUtility;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JsonWebTokenAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenAuthenticationProvider.class);

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticatedUser = null;
        if (authentication.getClass().isAssignableFrom(PreAuthenticatedAuthenticationToken.class) && authentication.getPrincipal() != null) {
            String tokenHeader = (String) authentication.getPrincipal();
            LOGGER.info("@@@ JsonWebTokenAuthenticationProvider AuthHeader : {}", tokenHeader);

            String token = extractTokenFromAuthHeader(tokenHeader);
            LOGGER.info("@@@ JsonWebTokenAuthenticationProvider Token : {}", token);

            UserDetails userDetails = null;
            try {
                userDetails = parseToken(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(userDetails)) {
                authenticatedUser = new JsonWebTokenAuthentication(userDetails, tokenHeader);
            }
        } else {
            // It is already a JsonWebTokenAuthentication
            authenticatedUser = authentication;
        }
        return authenticatedUser;
    }

    public String extractTokenFromAuthHeader(String authorizationHeader) {
        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            LOGGER.info("Bad Request Header Credentials: {}", authorizationHeader);
            throw new BadCredentialsException("Bad Request Header Credentials");
        }
        return authorizationHeader.substring(7);
    }

    private UserDetails parseToken(String tokenHeader) {
        UserDetails principal = null;

        String username = jwtUtility.extractUsername(tokenHeader);
        LOGGER.info("Extracted JWT for username:  {}", username);

        Claims claims = jwtUtility.extractClaims(tokenHeader);
        LOGGER.info("Extracted JWT for claims  : {}", claims);

        List<String> authoritiesList = (List<String>) claims.get("roles");
        LOGGER.info("Extracted JWT for authoritiesList : {}", authoritiesList);

        if (!claims.isEmpty() && StringUtils.isNotEmpty(username)) {
            List<GrantedAuthority> authorities = authoritiesList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            principal = new User(username, "", authorities);
        }

        return principal;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class) || authentication.isAssignableFrom(JsonWebTokenAuthentication.class);
    }

}
