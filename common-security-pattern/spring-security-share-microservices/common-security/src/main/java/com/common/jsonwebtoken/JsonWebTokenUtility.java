package com.common.jsonwebtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JsonWebTokenUtility {
    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenUtility.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;


    public String createJsonWebToken(AuthTokenDetailsDTO authTokenDetailsDTO) {
        logger.info("createJsonWebToken()...");
        return Jwts.builder()
                .setSubject(authTokenDetailsDTO.getId())
                .claim("id", authTokenDetailsDTO.getId())
                .claim("username", authTokenDetailsDTO.getUsername())
                .claim("email", authTokenDetailsDTO.getEmail())
                .claim("roles", authTokenDetailsDTO.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Claims extractClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public AuthTokenDetailsDTO parseAndValidate(String token) {
        try {
            Claims claims = extractClaimsFromToken(token);
            return createTokenDTOFromClaims(claims);
        } catch (Exception ex) {
            logger.info("@@@ parseAndValidate : {} ", ex.getMessage());
            return null;
        }
    }

    private AuthTokenDetailsDTO createTokenDTOFromClaims(Claims claims) {
        AuthTokenDetailsDTO authTokenDetailsDTO = new AuthTokenDetailsDTO();
        authTokenDetailsDTO.setId(String.valueOf(claims.get("id")));
        authTokenDetailsDTO.setEmail(String.valueOf(claims.get("email")));
        authTokenDetailsDTO.setUsername(String.valueOf(claims.get("username")));
        authTokenDetailsDTO.setRoles((List<String>) claims.get("roles"));
        authTokenDetailsDTO.setExpirationDate(claims.getExpiration());
        return authTokenDetailsDTO;
    }

    public boolean isTokenExpired(String token) {
        return extractClaimsFromToken(token).getExpiration().before(new Date());
    }

}
