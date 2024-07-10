package com.prunny.jwt_project.security.config;

import com.prunny.jwt_project.security.filters.CustomAuthorizationFilter;
import com.prunny.jwt_project.security.filters.CustomUsernamePasswordAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;
    private final RsaKeyProperties rsaKeys;
    private final CustomAuthorizationFilter authorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var authenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager, rsaKeys);
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .addFilterAt(authenticationFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(authorizationFilter, CustomUsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(POST,"/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/user/admin").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .build();
    }

}
