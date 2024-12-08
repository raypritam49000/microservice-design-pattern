package com.course.service.secuirty;

import com.common.jsonwebtoken.AuthenticationFilter;
import com.common.security.JsonWebTokenSecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ComponentScan(basePackages = "com.common")
public class CourseWebSecurityConfig extends JsonWebTokenSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(CourseWebSecurityConfig.class);

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> createFilterRegistrationBean() {
        logger.info("@@@ CourseWebSecurityConfig FilterRegistrationBean ::: ");
        return super.createFilterRegistrationBean(List.of("/course/*"));
    }
}
