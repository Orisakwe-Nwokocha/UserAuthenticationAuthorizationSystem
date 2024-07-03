package com.prunny.jwt_project.security.services;

import com.prunny.jwt_project.data.models.User;
import com.prunny.jwt_project.data.repositories.UserRepository;
import com.prunny.jwt_project.security.models.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Trying to get user by username: {}", username);
        User user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(()-> new UsernameNotFoundException("Invalid username or password"));
        log.info("Found user with username: {}", username);
        return new UserDetailsImpl(user);
    }
}
