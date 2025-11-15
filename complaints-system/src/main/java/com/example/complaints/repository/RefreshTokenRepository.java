package com.example.complaints.repository;

import com.example.complaints.entity.RefreshToken;
import com.example.complaints.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);
    void deleteByUser(User user);
    void deleteByExpiresAtBefore(Instant now);
}

