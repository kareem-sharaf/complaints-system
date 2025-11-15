package com.example.complaints.dao;

import com.example.complaints.entity.User;

import java.util.Optional;

public interface UserDao {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailOrPhone(String identifier);
    void delete(User user);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}

