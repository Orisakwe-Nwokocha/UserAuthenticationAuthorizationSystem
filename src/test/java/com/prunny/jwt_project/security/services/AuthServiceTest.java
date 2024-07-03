package com.prunny.jwt_project.security.services;

import com.prunny.jwt_project.dto.requests.RegisterRequest;
import com.prunny.jwt_project.dto.responses.RegisterResponse;
import com.prunny.jwt_project.exceptions.UsernameExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static com.prunny.jwt_project.utils.TestUtils.buildRegisterRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(scripts = {"/db/data.sql"})
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void registerUserTest() {
        RegisterRequest request = buildRegisterRequest();
        var response = authService.register(request);
        assertThat(response).isNotNull();
        RegisterResponse responseData = response.getData();
        assertThat(responseData.getId()).isNotNull();
        assertThat(responseData.getMessage()).contains("success");
    }

    @Test
    @DisplayName("test that registration fails for non unique username")
    public void registerUserWithExistingUsername_throwsExceptionTest() {
        RegisterRequest request = buildRegisterRequest();
        var response = authService.register(request);
        assertThat(response).isNotNull();
        assertThrows(UsernameExistsException.class, ()-> authService.register(request));
    }


}