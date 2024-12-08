package com.common.security;

import com.common.jsonwebtoken.AuthTokenDetailsDTO;
import com.common.jsonwebtoken.JsonWebTokenUtility;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {
    private final Logger LOGGER = LoggerFactory.getLogger(RequestHeaderAuthenticationProvider.class.getName());

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JsonWebTokenUtility jwtUtility;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String authorizationHeader = String.valueOf(authentication.getPrincipal());
            LOGGER.info("@@@ RequestHeaderAuthenticationProvider authenticate : {}", authorizationHeader);

            if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                LOGGER.warn("Bad Request Header Credentials: {}", authorizationHeader);
                throw new BadCredentialsException("Bad Request Header Credentials");
            }

            String jwt = authorizationHeader.substring(7);
            UserDetails userDetails = parseToken(jwt);

            //return new PreAuthenticatedAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

            if (Objects.nonNull(userDetails) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                PreAuthenticatedAuthenticationToken usernamePasswordAuthenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                return usernamePasswordAuthenticationToken;
            }
            return null;
        } catch (Exception ex) {
            LOGGER.error("Exception Occur when inside RequestHeaderAuthenticationProvider call authenticate method : {}" , ex.getMessage());
            throw new BadCredentialsException(ex.getMessage());
        }
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
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}