package com.example.complaints.service.impl;

import com.example.complaints.dao.RefreshTokenDao;
import com.example.complaints.dao.RoleDao;
import com.example.complaints.dao.UserDao;
import com.example.complaints.dto.AuthResponse;
import com.example.complaints.dto.LoginRequest;
import com.example.complaints.dto.LogoutRequest;
import com.example.complaints.dto.RefreshTokenRequest;
import com.example.complaints.dto.RegisterRequest;
import com.example.complaints.entity.RefreshToken;
import com.example.complaints.entity.Role;
import com.example.complaints.entity.RoleName;
import com.example.complaints.entity.User;
import com.example.complaints.exception.AppException;
import com.example.complaints.exception.UnauthorizedException;
import com.example.complaints.security.jwt.JwtTokenProvider;
import com.example.complaints.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final RefreshTokenDao refreshTokenDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserDao userDao,
                           RoleDao roleDao,
                           RefreshTokenDao refreshTokenDao,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.refreshTokenDao = refreshTokenDao;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userDao.existsByEmail(request.email())) {
            throw new AppException("Email already registered");
        }
        if (userDao.existsByPhone(request.phone())) {
            throw new AppException("Phone already registered");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(fetchRole(RoleName.ROLE_USER)));

        User savedUser = userDao.save(user);
        return issueTokens(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userDao.findByEmailOrPhone(request.identifier())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return issueTokens(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenDao.findActiveToken(request.refreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (!tokenProvider.validateToken(storedToken.getToken())) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        storedToken.setRevoked(true);
        refreshTokenDao.save(storedToken);

        User user = storedToken.getUser();
        return issueTokens(user);
    }

    @Override
    public void logout(LogoutRequest request) {
        RefreshToken storedToken = refreshTokenDao.findActiveToken(request.refreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        storedToken.setRevoked(true);
        refreshTokenDao.save(storedToken);
    }

    private AuthResponse issueTokens(User user) {
        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshTokenValue = tokenProvider.generateRefreshToken(user);
        persistRefreshToken(user, refreshTokenValue);
        long expiresInSeconds = tokenProvider.getAccessExpirationMs() / 1000;
        return AuthResponse.of(accessToken, refreshTokenValue, expiresInSeconds);
    }

    private void persistRefreshToken(User user, String tokenValue) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(tokenValue);
        refreshToken.setExpiresAt(Instant.now().plusMillis(tokenProvider.getRefreshExpirationMs()));
        refreshTokenDao.save(refreshToken);
    }

    private Role fetchRole(RoleName name) {
        return roleDao.findByName(name)
                .orElseGet(() -> roleDao.save(new Role(name)));
    }
}

