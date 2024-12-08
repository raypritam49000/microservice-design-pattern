package com.api.gateway.controller;

import com.api.gateway.model.AuthTokenModel;
import com.api.gateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/api")
public class HystrixController {
	
	@Autowired
	private AuthService authService;
	
//	@GetMapping("/test")
//	public AuthTokenModel testServiceFallback() {
//		return authService.getJWTToken("1231");
//	}

}