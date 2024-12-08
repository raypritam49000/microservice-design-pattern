package com.client.service.security;

import com.client.service.config.PropertySource;
import com.client.service.exception.AuthException;
import com.client.service.util.TokenClaim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthProvider implements AuthenticationProvider {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JwtAuthManager jwtAuthManager;

    @Autowired
    private PropertySource propertySource;

    @Override
    public Authentication authenticate(final Authentication authentication) {
        logger.info("@@@ Inside AuthProvider authenticate method : {}", authentication);
        final AuthenticationModel authenticationModel = (AuthenticationModel) authentication;

        try {
            doAuthorization(authenticationModel);
            SecurityContextHolder.getContext().setAuthentication(authenticationModel);
        } catch (Exception e) {
            final String msg = String.format("Authorization Failed %s", e.getMessage());
            throw new AuthException(msg, e);
        }
        return authenticationModel;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationModel.class.isAssignableFrom(authentication);
    }

    private final void doAuthorization(final AuthenticationModel appAuthUserModel) {
        logger.info("@@@ Inside AuthProvider doAuthorization method AuthenticationModel : {}", appAuthUserModel);

        final String cleansedToken = validateToken(appAuthUserModel.getCredentials());

        logger.info("@@@ Inside AuthProvider doAuthorization method cleansedToken : {}", cleansedToken);

        final Jws<Claims> claims = jwtAuthManager.validateToken(cleansedToken);

        logger.info("@@@ Inside AuthProvider doAuthorization method Claims : {}", claims);

        appAuthUserModel.setAuthorities(getAuthorities(claims));
        appAuthUserModel.setCredentials("TBS");
        appAuthUserModel.setDetails(claims.getBody().get(TokenClaim.USER_ID.getKey(), String.class));
        appAuthUserModel.setName(claims.getBody().get(TokenClaim.USER_INFO.getKey(), String.class));
        appAuthUserModel.setPrincipal(getAuthUserModel(claims));
        appAuthUserModel.setAuthenticated(true);
    }

    private final String validateToken(final String headerValue) {
        logger.info("@@@ Inside AuthProvider validateToken method headerValue : {}", headerValue);
        if (ObjectUtils.isEmpty(headerValue)) throw new AuthException("Authorization Header is empty");
        final String token = headerValue.replace("Bearer", "");
        if (ObjectUtils.isEmpty(token)) throw new AuthException("Token is Empty");
        return token;
    }

    @SuppressWarnings("unchecked")
    private final List<AuthRoleModel> getAuthorities(Jws<Claims> claims) {
        logger.info("@@@ Inside AuthProvider getAuthorities method Claims : {}", claims);
        List<String> authorities = claims.getBody().get(TokenClaim.AUTHORITIES.getKey(), List.class);
        if (ObjectUtils.isEmpty(authorities) || !authorities.contains(propertySource.getAppAuthRoleAllowed()))
            throw new AuthException("Invalid Role");
        return authorities.stream().map(AuthRoleModel::new).collect(Collectors.toList());
    }

    private final AuthUserModel getAuthUserModel(Jws<Claims> claims) {
        logger.info("@@@ Inside AuthProvider getAuthUserModel method Claims : {}", claims);
        final AuthUserModel authUserModel = new AuthUserModel();
        authUserModel.setUserName(claims.getBody().get(TokenClaim.USER_INFO.getKey(), String.class));
        return authUserModel;
    }

}
