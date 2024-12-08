package com.api.gateway.service.models;

import lombok.*;

import java.util.Collection;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String userId;

    private String sessionId;
    private String accessToken;
    private String refreshToken;
    private long expireAt;

    private Collection<String> authorities;
}
