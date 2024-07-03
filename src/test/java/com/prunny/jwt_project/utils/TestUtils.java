package com.prunny.jwt_project.utils;

import com.prunny.jwt_project.dto.requests.RegisterRequest;

import static com.prunny.jwt_project.data.constants.Role.USER;

public class TestUtils {

    public static RegisterRequest buildRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setRole(USER);
        return request;
    }
}
