package com.client.service.security;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


@Setter
public class AuthRoleModel implements GrantedAuthority {

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    public AuthRoleModel(String authority) {
        super();
        this.authority = authority;
    }

}
