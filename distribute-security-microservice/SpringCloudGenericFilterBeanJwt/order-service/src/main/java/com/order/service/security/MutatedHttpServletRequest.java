package com.order.service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class MutatedHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public MutatedHttpServletRequest(HttpServletRequest request, Claims claims) {
        super(request);
        this.customHeaders = new HashMap<>();
        this.customHeaders.put("id", String.valueOf(claims.get("id")));
        this.customHeaders.put("role", String.valueOf(claims.get("role")));
        this.customHeaders.put("username", String.valueOf(claims.get("username")));
        this.customHeaders.put("email", String.valueOf(claims.get("email")));
    }

    @Override
    public String getHeader(String name) {
        return customHeaders.getOrDefault(name, super.getHeader(name));
    }
}

