package com.prunny.jwt_project.security.utils;

import java.util.List;

public class SecurityUtils {
    private SecurityUtils() {}

    public static final List<String> PUBLIC_ENDPOINTS = List.of("/api/v1/auth");

    public static final String JWT_PREFIX = "Bearer ";
}
