package com.prunny.jwt_project.dto.requests;

import com.prunny.jwt_project.data.constants.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
