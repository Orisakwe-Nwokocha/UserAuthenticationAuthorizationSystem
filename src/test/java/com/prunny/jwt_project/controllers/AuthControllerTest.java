package com.prunny.jwt_project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prunny.jwt_project.dto.requests.LoginRequest;
import com.prunny.jwt_project.dto.requests.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.prunny.jwt_project.utils.TestUtils.buildRegisterRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = {"/db/data.sql"})
    void registerUserTest() throws Exception {
        RegisterRequest request = buildRegisterRequest();
        byte[] content = new ObjectMapper().writeValueAsBytes(request);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    public void authenticateUserTest() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("password");
        byte[] content = new ObjectMapper().writeValueAsBytes(request);
        mockMvc.perform(post("/api/v1/auth")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void authenticateUserTest_FailsForInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("wrongPassword");
        byte[] content = new ObjectMapper().writeValueAsBytes(request);
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}