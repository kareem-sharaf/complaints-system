package com.example.complaints.complaint;

import com.example.complaints.complaint.dto.CreateComplaintRequest;
import com.example.complaints.complaint.dto.UpdateComplaintStatusRequest;
import com.example.complaints.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Transactional(readOnly = true)
    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Complaint findById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint %d not found".formatted(id)));
    }

    @Transactional
    public Complaint create(CreateComplaintRequest request) {
        Complaint complaint = new Complaint();
        complaint.setSubject(request.subject());
        complaint.setDescription(request.description());
        complaint.setReporterName(request.reporterName());
        complaint.setReporterEmail(request.reporterEmail());
        complaint.setCategory(request.category());
        complaint.setStatus(ComplaintStatus.OPEN);
        return complaintRepository.save(complaint);
    }

    @Transactional
    public Complaint updateStatus(Long id, UpdateComplaintStatusRequest request) {
        Complaint complaint = findById(id);
        complaint.setStatus(request.status());
        return complaintRepository.save(complaint);
    }

    @Transactional
    public void delete(Long id) {
        Complaint complaint = findById(id);
        complaintRepository.delete(complaint);
    }
}

