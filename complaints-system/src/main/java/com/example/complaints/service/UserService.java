package com.example.complaints.service;

import com.example.complaints.dto.UpdateUserRequest;
import com.example.complaints.dto.UserResponse;

public interface UserService {
    UserResponse findById(Long id);
    UserResponse findByEmail(String email);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
}

