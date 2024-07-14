package com.prunny.jwt_project.security.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.NONE;

@Entity
@Table(name = "blacklisted_tokens")
@Getter
@Setter
@ToString
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false, length = 600)
    private String token;
    private Instant expiresAt;
    @Setter(NONE)
    private LocalDateTime blacklistedAt;

    @PrePersist
    private void setBlacklistedAt() {
        blacklistedAt = now();
    }
}
