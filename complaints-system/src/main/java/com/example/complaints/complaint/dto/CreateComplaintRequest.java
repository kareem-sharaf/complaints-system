package com.example.complaints.complaint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateComplaintRequest(
        @NotBlank @Size(max = 120) String subject,
        @NotBlank @Size(max = 2000) String description,
        @NotBlank @Size(max = 120) String reporterName,
        @NotBlank @Email @Size(max = 160) String reporterEmail,
        @Size(max = 60) String category
) {
}

