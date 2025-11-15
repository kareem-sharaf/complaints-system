package com.example.complaints.dto;

import java.util.Set;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Set<String> roles
) {
}

