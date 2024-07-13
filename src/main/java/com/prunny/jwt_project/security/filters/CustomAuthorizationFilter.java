package com.prunny.jwt_project.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prunny.jwt_project.security.config.RsaKeyProperties;
import com.prunny.jwt_project.security.services.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.prunny.jwt_project.security.utils.SecurityUtils.JWT_PREFIX;
import static com.prunny.jwt_project.security.utils.SecurityUtils.PUBLIC_ENDPOINTS;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@AllArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final RsaKeyProperties rsaKeys;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Starting authorization");
        String requestPath = request.getRequestURI();
        boolean isRequestPathPublic = PUBLIC_ENDPOINTS.contains(requestPath);
        if (isRequestPathPublic) {
            log.info("Authorization not needed for public endpoint: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null) {
            String token = authorizationHeader.substring(JWT_PREFIX.length()).strip();
            if (isTokenBlacklisted(response, token)) return;
            doAuthorization(token);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isTokenBlacklisted(HttpServletResponse response, String token) throws IOException {
        if (authService.isTokenBlacklisted(token)) {
            log.warn("Token is blacklisted: {}", token);
            response.sendError(SC_UNAUTHORIZED, "Unauthorized");
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"Token is expired or invalid\"}");
            response.getWriter().flush();
            return true;
        }
        return false;
    }

    private void doAuthorization(String token) {
        Algorithm algorithm = Algorithm.RSA512(rsaKeys.publicKey(), rsaKeys.privateKey());
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withIssuer("jwt-project")
                .withClaimPresence("roles")
                .withClaimPresence("principal")
                .withClaimPresence("credentials")
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        List<? extends GrantedAuthority> authorities = decodedJWT.getClaim("roles")
                .asList(SimpleGrantedAuthority.class);
        String principal = decodedJWT.getClaim("principal").asString();
        String credentials = decodedJWT.getClaim("credentials").asString();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User authorization succeeded");
    }
}
