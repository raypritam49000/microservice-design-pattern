package com.user.service.security;

import com.common.security.JsonWebTokenSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = "com.common")
public class UserWebSecurityConfig extends JsonWebTokenSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(UserWebSecurityConfig.class);

    @Override
    protected void setupAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        );
    }

}
