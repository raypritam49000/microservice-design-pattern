package com.auth_service.crypto;

public interface PasswordEncoder {
    public String encode(CharSequence rawPassword);
    public boolean matches(CharSequence rawPassword, String encodedPassword);
}
