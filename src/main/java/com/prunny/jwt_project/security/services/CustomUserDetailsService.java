package com.prunny.jwt_project.security.services;

import com.prunny.jwt_project.data.models.User;
import com.prunny.jwt_project.data.repositories.UserRepository;
import com.prunny.jwt_project.security.models.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(()-> new UsernameNotFoundException("Invalid username or password"));
        return new UserDetailsImpl(user);
    }
}
