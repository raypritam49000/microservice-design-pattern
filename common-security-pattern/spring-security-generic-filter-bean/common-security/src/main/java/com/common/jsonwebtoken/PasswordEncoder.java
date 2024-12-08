package com.common.jsonwebtoken;

public interface PasswordEncoder {
    public String encode(String rawPassword);

    public boolean matches(String rawPassword, String encodedPassword);
}
