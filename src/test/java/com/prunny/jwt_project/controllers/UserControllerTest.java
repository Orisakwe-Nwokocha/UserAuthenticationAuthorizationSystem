package com.prunny.jwt_project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prunny.jwt_project.dto.requests.LoginRequest;
import com.prunny.jwt_project.dto.responses.ApiResponse;
import com.prunny.jwt_project.dto.responses.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/db/data.sql"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("test that home endpoint is open for any authorized user")
    void homeTest() throws Exception {
        String token = getToken("admin");
        mockMvc.perform(get("/api/v1/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Hello, admin"))
                .andDo(print());

        token = getToken("user");
        mockMvc.perform(get("/api/v1/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Hello, user"))
                .andDo(print());
    }

    @Test
    @DisplayName("test that admin endpoint is open for only authorized users with admin role")
    @WithMockUser(authorities = "ADMIN")
    void greetAdminTest() throws Exception {
        mockMvc.perform(get("/api/v1/user/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, user"))
                .andDo(print());
    }

    @Test
    @DisplayName("test that admin endpoint is open for only authorized users with admin role")
    @WithMockUser(authorities = "USER")
    void greetAdminTest_FailsForUsersWithUnauthorisedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/user/admin"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    public String getToken(String username) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword("password");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] content = objectMapper.writeValueAsBytes(request);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                                            .contentType(APPLICATION_JSON)
                                            .content(content))
                                    .andExpect(status().isOk())
                                    .andReturn();
        ApiResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), ApiResponse.class);
        LoginResponse loginResponse = modelMapper.map(response.getData(), LoginResponse.class);
        return loginResponse.getToken();
    }
}