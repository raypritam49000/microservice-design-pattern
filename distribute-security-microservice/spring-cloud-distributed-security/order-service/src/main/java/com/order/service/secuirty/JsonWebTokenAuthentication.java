package com.order.service.secuirty;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class JsonWebTokenAuthentication extends AbstractAuthenticationToken {
    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenAuthentication.class);

    private final UserDetails principal;
    private final String jsonWebToken;

    public JsonWebTokenAuthentication(UserDetails principal, String jsonWebToken) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.jsonWebToken = jsonWebToken;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
