package com.example.complaints.complaint.dto;

import com.example.complaints.complaint.ComplaintStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateComplaintStatusRequest(
        @NotNull ComplaintStatus status
) {
}

