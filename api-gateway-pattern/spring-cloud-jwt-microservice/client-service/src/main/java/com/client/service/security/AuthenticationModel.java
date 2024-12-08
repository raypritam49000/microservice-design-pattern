package com.client.service.security;

import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Setter
@ToString
public class AuthenticationModel implements Authentication {

    private String name;

    private List<AuthRoleModel> authorities;

    private String credentials;

    private String details;

    private AuthUserModel principal;

    private boolean authenticated;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public AuthUserModel getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
