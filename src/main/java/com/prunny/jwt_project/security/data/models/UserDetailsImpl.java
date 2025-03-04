package com.prunny.jwt_project.security.data.models;

import com.prunny.jwt_project.data.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "[" +
                "Username=" + user.getUsername() +
                ", Password=" + "[PROTECTED]" +
                ", Enabled=" + isEnabled() +
                ", AccountNonExpired=" + isAccountNonExpired() +
                ", CredentialsNonExpired=" + isCredentialsNonExpired() +
                ", AccountNonLocked=" + isAccountNonLocked() +
                ", Granted Authorities=" + getAuthorities() +
                ']';
    }

}
