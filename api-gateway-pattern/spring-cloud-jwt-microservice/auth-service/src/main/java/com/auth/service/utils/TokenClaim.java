package com.auth.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenClaim {

	USER_ID("userId"), USER_INFO("UserInfo"), AUTHORITIES("authorities");

	private final String key;
}
