package com.course.service.secuirty;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String token;

    public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
        this.token = token;
    }

    public JwtAuthenticationToken(UserDetails principal, String token) {
        super(principal, null, principal.getAuthorities());
        this.token = token;
    }

}
