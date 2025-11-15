package com.example.complaints.complaint;

import com.example.complaints.complaint.dto.CreateComplaintRequest;
import com.example.complaints.complaint.dto.UpdateComplaintStatusRequest;
import com.example.complaints.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Complaint>>> list() {
        return ResponseEntity.ok(
                ApiResponse.success("Complaints fetched", complaintService.findAll())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Complaint>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Complaint fetched", complaintService.findById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Complaint>> create(@Valid @RequestBody CreateComplaintRequest request) {
        Complaint created = complaintService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Complaint created", created));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Complaint>> updateStatus(@PathVariable Long id,
                                  @Valid @RequestBody UpdateComplaintStatusRequest request) {
        Complaint updated = complaintService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Complaint status updated", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        complaintService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Complaint deleted", null));
    }
}

