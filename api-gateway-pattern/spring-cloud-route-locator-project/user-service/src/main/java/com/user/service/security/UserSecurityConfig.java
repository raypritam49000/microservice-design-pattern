package com.user.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserSecurityConfig {
    
    @Autowired
    private JwtUtility jwtUtility;

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<AuthenticationFilter>();
        filterRegistrationBean.setFilter(new AuthenticationFilter(jwtUtility));
        filterRegistrationBean.addUrlPatterns("/howdy/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

}