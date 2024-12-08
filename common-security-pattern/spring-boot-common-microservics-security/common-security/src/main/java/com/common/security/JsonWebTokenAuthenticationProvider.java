package com.common.security;

import com.common.jsonwebtoken.AuthTokenDetailsDTO;
import com.common.jsonwebtoken.JsonWebTokenUtility;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JsonWebTokenAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWebTokenAuthenticationProvider.class);

    @Autowired
    private JsonWebTokenUtility jwtUtility;

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
        AuthTokenDetailsDTO authTokenDetailsDTO = jwtUtility.parseAndValidate(tokenHeader);
        LOGGER.info("@@@ parseToken authTokenDetailsDTO : {}", authTokenDetailsDTO);
        if (Objects.nonNull(authTokenDetailsDTO)) {
            principal = new User(authTokenDetailsDTO.getUsername(), "", authTokenDetailsDTO.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        }
        return principal;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class) || authentication.isAssignableFrom(JsonWebTokenAuthentication.class);
    }

}
