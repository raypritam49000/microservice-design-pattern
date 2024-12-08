package com.client.service.security;

import com.client.service.config.PropertySource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;

@Component
public class JwtAuthManager {

	@Autowired
	private PropertySource propertySource;

	public Jws<Claims> validateToken(final String token) {
		final byte[] encodedSecretBytes = DatatypeConverter.parseBase64Binary(propertySource.getAppAuthJwtSecret());
		return Jwts.parser().setSigningKey(encodedSecretBytes).parseClaimsJws(token);
	}

}
