package com.prunny.jwt_project.security.data.repositories;

import com.prunny.jwt_project.security.data.models.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
}
