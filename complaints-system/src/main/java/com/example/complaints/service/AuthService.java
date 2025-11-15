package com.example.complaints.service;

import com.example.complaints.dto.AuthResponse;
import com.example.complaints.dto.LoginRequest;
import com.example.complaints.dto.LogoutRequest;
import com.example.complaints.dto.RefreshTokenRequest;
import com.example.complaints.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(LogoutRequest request);
}

