package com.prunny.jwt_project.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prunny.jwt_project.security.config.RsaKeyProperties;
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
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@AllArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final RsaKeyProperties rsaKeys;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Starting authorization");
        String requestPath = request.getServletPath();
        boolean isRequestPathPublic = PUBLIC_ENDPOINTS.contains(requestPath);
        if (isRequestPathPublic) filterChain.doFilter(request, response);
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null) {
            log.info("Authorization not needed for public endpoint: {}", requestPath);
            doAuthorization(authorizationHeader);
        }
        filterChain.doFilter(request, response);
    }

    private void doAuthorization(String authorizationHeader) {
        String token = authorizationHeader.substring(JWT_PREFIX.length()).strip();
        Algorithm algorithm = Algorithm.RSA256(rsaKeys.publicKey(), rsaKeys.privateKey());
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
