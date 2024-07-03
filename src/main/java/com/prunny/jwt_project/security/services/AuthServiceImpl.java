package com.prunny.jwt_project.security.services;

import com.prunny.jwt_project.data.models.User;
import com.prunny.jwt_project.data.repositories.UserRepository;
import com.prunny.jwt_project.dto.requests.RegisterRequest;
import com.prunny.jwt_project.dto.responses.ApiResponse;
import com.prunny.jwt_project.dto.responses.RegisterResponse;
import com.prunny.jwt_project.exceptions.UsernameExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Override
    public ApiResponse<RegisterResponse> register(RegisterRequest request) {
        log.info("Trying to register new: {}", request.getRole());
        String username = request.getUsername().toLowerCase();
        validate(username);
        request.setUsername(username);
        User newUser = registerNewUser(request);
        RegisterResponse registerResponse = mapper.map(newUser, RegisterResponse.class);
        registerResponse.setMessage("User registered successfully");
        log.info("{} user registered successfully", request.getRole());
        return new ApiResponse<>(now(), true, registerResponse);
    }

    private User registerNewUser(RegisterRequest request) {
        User newUser = mapper.map(request, User.class);
        newUser.setRoles(new HashSet<>());
        newUser.getRoles().add(request.getRole());
        return userRepository.save(newUser);
    }

    private void validate(String username) {
        boolean usernameExists = userRepository.existsByUsername(username);
        if (usernameExists) {
            log.error("Username '{}' is already taken", username);
            throw new UsernameExistsException("Username is already taken");
        }
    }
}
