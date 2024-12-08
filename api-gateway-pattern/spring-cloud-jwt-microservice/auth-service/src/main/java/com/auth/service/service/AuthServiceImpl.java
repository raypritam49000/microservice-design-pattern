package com.auth.service.service;

import com.auth.service.config.PropertySource;
import com.auth.service.exception.AuthException;
import com.auth.service.model.AuthTokenModel;
import com.auth.service.repo.DataStore;
import com.auth.service.utils.JWTHelper;
import com.auth.service.utils.TokenClaim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private DataStore dataStore;

	@Autowired
	private PropertySource propertySource;

	@Override
	public AuthTokenModel validateApiKeyAndGetJwtToken(final String apiKey) {
		try {
			final String userId = validateApiKeyAndGetUserId(apiKey);
			final Map<String, Object> claims = getUserInfo(userId);
			final String jwtTokenValue = JWTHelper.createJWT(claims, propertySource.getAppName(),
					propertySource.getAppAuthSecret(), propertySource.getAppTimeToLive());
			return getTokenModel(jwtTokenValue);
		} catch (Exception e) {
			throw new AuthException("Unauthorized API key : " + apiKey, e);
		}
	}

	private final String validateApiKeyAndGetUserId(final String apiKey) {
		return Optional.ofNullable(dataStore.getUserIdForApikey(apiKey))
				.orElseThrow(() -> new AuthException("InValid API Key"));
	}

	private final Map<String, Object> getUserInfo(final String userId) {
		final Map<String, Object> claims = new HashMap<>();
		final String userInfo = dataStore.getUserInfo(userId);
		final String userRole = dataStore.getUserRole(userId);
		final List<String> authorities = new ArrayList<>();
		authorities.add(userRole);
		claims.put(TokenClaim.USER_ID.getKey(), userId);
		claims.put(TokenClaim.USER_INFO.getKey(), userInfo);
		claims.put(TokenClaim.AUTHORITIES.getKey(), authorities);
		return claims;
	}

	private final AuthTokenModel getTokenModel(final String jwtTokenValue) {
		final AuthTokenModel tokenModel = new AuthTokenModel();
		tokenModel.setType(propertySource.getAppAuthTokenType());
		tokenModel.setToken(jwtTokenValue);
		return tokenModel;
	}

}