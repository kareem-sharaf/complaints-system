package com.example.complaints.dao;

import com.example.complaints.entity.RefreshToken;
import com.example.complaints.entity.User;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenDao {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findActiveToken(String token);
    void deleteByUser(User user);
    void deleteExpired(Instant now);
}

