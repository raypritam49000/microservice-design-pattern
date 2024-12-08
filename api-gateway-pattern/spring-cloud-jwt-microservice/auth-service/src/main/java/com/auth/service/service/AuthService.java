package com.auth.service.service;


import com.auth.service.model.AuthTokenModel;

public interface AuthService {

	public AuthTokenModel validateApiKeyAndGetJwtToken(String apiKey);

}
