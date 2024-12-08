package com.common.security;

import com.common.jsonwebtoken.AuthenticationFilter;
import com.common.jsonwebtoken.BCryptPasswordEncoder;
import com.common.jsonwebtoken.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.List;


public abstract class JsonWebTokenSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenSecurityConfig.class);

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("passwordEncoder()...");
        return new BCryptPasswordEncoder();
    }

    public FilterRegistrationBean<AuthenticationFilter> createFilterRegistrationBean(List<String> urlPatterns) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<AuthenticationFilter>();
        registrationBean.setFilter(authenticationFilter);
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
