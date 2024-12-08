package com.client.service.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;



@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertySource {

	@Value("${app.auth.jwt.secret}")
	private String appAuthJwtSecret;

	@Value("${app.auth.role.allowed}")
	private String appAuthRoleAllowed;
}
