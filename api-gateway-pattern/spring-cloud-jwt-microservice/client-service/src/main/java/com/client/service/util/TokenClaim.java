package com.client.service.util;

import lombok.Getter;

@Getter
public enum TokenClaim {
    USER_ID("userId"), USER_INFO("UserInfo"), AUTHORITIES("authorities");

    private final String key;

    private TokenClaim(final String key) {
        this.key = key;
    }
}
