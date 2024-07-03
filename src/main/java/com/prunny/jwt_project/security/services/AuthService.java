package com.prunny.jwt_project.security.services;

import com.prunny.jwt_project.dto.requests.RegisterRequest;
import com.prunny.jwt_project.dto.responses.ApiResponse;
import com.prunny.jwt_project.dto.responses.RegisterResponse;

public interface AuthService {
    ApiResponse<RegisterResponse> register(RegisterRequest request);
}
