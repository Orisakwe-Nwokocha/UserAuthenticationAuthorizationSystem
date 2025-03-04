package com.prunny.jwt_project.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prunny.jwt_project.dto.requests.LoginRequest;
import com.prunny.jwt_project.dto.responses.ApiResponse;
import com.prunny.jwt_project.dto.responses.ErrorResponse;
import com.prunny.jwt_project.dto.responses.LoginResponse;
import com.prunny.jwt_project.security.config.RsaKeyProperties;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper = new ObjectMapper();

    private final AuthenticationManager authenticationManager;
    private final RsaKeyProperties rsaKeys;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        log.info("Starting user authentication");
        LoginRequest loginRequest;
        try {
            InputStream inputStream = request.getInputStream();
            loginRequest = mapper.readValue(inputStream, LoginRequest.class);
        } catch (IOException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
        String username = loginRequest.getUsername().toLowerCase();
        String password = loginRequest.getPassword();
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authResult = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        log.info("Retrieved the authentication result from authentication manager");
        return  authResult;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(generateAccessToken(authResult));
        loginResponse.setMessage("Authentication succeeded");
        ApiResponse<LoginResponse> apiResponse =
                new ApiResponse<>(now(), true, loginResponse);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(apiResponse));
        response.flushBuffer();
        log.info("User authentication successful");
        chain.doFilter(request, response);
    }

    private String generateAccessToken(Authentication authResult) {
        Algorithm algorithm = Algorithm.RSA512(rsaKeys.publicKey(), rsaKeys.privateKey());
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer("jwt-project")
                .withIssuedAt(now)
                .withExpiresAt(now.plus(1, HOURS))
                .withSubject(authResult.getPrincipal().toString())
                .withClaim("principal", authResult.getPrincipal().toString())
                .withClaim("credentials", authResult.getCredentials().toString())
                .withArrayClaim("roles", extractAuthorities(authResult.getAuthorities()))
                .sign(algorithm);
    }

    private String[] extractAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException exception)
            throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("UnsuccessfulAuthentication")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        response.sendError(SC_UNAUTHORIZED, "Unauthorized");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(errorResponse));
        response.flushBuffer();
        log.info("User authentication unsuccessful");
    }

}
