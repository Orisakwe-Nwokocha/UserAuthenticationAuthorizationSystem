package com.prunny.jwt_project.data.models;

import com.prunny.jwt_project.data.constants.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.NONE;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;

    @ElementCollection(fetch = EAGER)
    @Enumerated(STRING)
    private Set<Role> roles;
    @Setter(NONE)
    private LocalDateTime dateRegistered;
    @Setter(NONE)
    private LocalDateTime dateUpdated;

    @PrePersist
    private void setDateRegistered() {
        dateRegistered = now();
    }

    @PreUpdate
    private void setDateUpdated() {
        dateUpdated = now();
    }

}
