package com.prunny.jwt_project.security.services;

import com.prunny.jwt_project.data.models.User;
import com.prunny.jwt_project.data.repositories.UserRepository;
import com.prunny.jwt_project.dto.requests.RegisterRequest;
import com.prunny.jwt_project.dto.responses.ApiResponse;
import com.prunny.jwt_project.dto.responses.RegisterResponse;
import com.prunny.jwt_project.exceptions.UsernameExistsException;
import com.prunny.jwt_project.security.data.models.BlacklistedToken;
import com.prunny.jwt_project.security.data.repositories.BlacklistedTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final BlacklistedTokenRepository blacklistedTokenRepository;

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

    @Override
    public void blacklist(String token) {
        log.info("Trying to blacklist token: {}", token);
        trackExpiredTokens();
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiresAt(Instant.now().plus(1, HOURS));
        blacklistedTokenRepository.save(blacklistedToken);
        log.info("Blacklisted token: {}", token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        log.info("Checking blacklist status of token: {}", token);
        boolean isBlacklisted = blacklistedTokenRepository.existsByToken(token);
        log.info("Blacklist status of token: {}", isBlacklisted);
        trackExpiredTokens();
        return isBlacklisted;
    }

    private void trackExpiredTokens() {
        log.info("Tracking and deleting expired user tokens");
        var blacklist = blacklistedTokenRepository.findAll();
        blacklist.stream()
                .filter(blacklistedToken -> Instant.now().isAfter(blacklistedToken.getExpiresAt()))
                .forEach(blacklistedTokenRepository::delete);
        log.info("Expired user tokens successfully tracked and deleted");
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
