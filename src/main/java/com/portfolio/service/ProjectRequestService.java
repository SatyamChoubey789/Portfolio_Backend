package com.portfolio.service;

import com.portfolio.model.ProjectRequest;
import com.portfolio.model.RequestResponse;
import com.portfolio.repository.ProjectRequestRepository;
import com.portfolio.repository.RequestResponseRepository;
import com.portfolio.dto.request.ProjectRequestDto;
import com.portfolio.dto.request.ResponseRequest;
import com.portfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRequestService {
    
    private final ProjectRequestRepository projectRequestRepository;
    private final RequestResponseRepository requestResponseRepository;
    private final EmailService emailService;
    
    @Transactional
    public ProjectRequest createRequest(ProjectRequestDto dto) {
        ProjectRequest request = ProjectRequest.builder()
            .clientName(dto.getClientName())
            .clientEmail(dto.getClientEmail())
            .clientPhone(dto.getClientPhone())
            .companyName(dto.getCompanyName())
            .projectTitle(dto.getProjectTitle())
            .projectType(dto.getProjectType())
            .budgetRange(dto.getBudgetRange())
            .timeline(dto.getTimeline())
            .description(dto.getDescription())
            .requirements(dto.getRequirements())
            .attachments(dto.getAttachments())
            .status("PENDING")
            .priority("NORMAL")
            .build();
        
        ProjectRequest saved = projectRequestRepository.save(request);
        
        // Send confirmation email to client
        emailService.sendProjectRequestConfirmation(
            saved.getClientEmail(),
            saved.getClientName(),
            saved.getId()
        );
        
        // Send alert to admin
        emailService.sendAdminNewRequestAlert(
            saved.getClientName(),
            saved.getProjectTitle(),
            saved.getId()
        );
        
        return saved;
    }
    
    public List<ProjectRequest> getAllRequests() {
        return projectRequestRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public List<ProjectRequest> getRequestsByStatus(String status) {
        return projectRequestRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public List<ProjectRequest> getRequestsByStatusAndPriority(String status, String priority) {
        if (status != null && priority != null) {
            return projectRequestRepository.findByStatusAndPriorityOrderByCreatedAtDesc(status, priority);
        } else if (status != null) {
            return projectRequestRepository.findByStatusOrderByCreatedAtDesc(status);
        } else if (priority != null) {
            return projectRequestRepository.findByPriorityOrderByCreatedAtDesc(priority);
        }
        return getAllRequests();
    }
    
    public ProjectRequest getRequestById(Long id) {
        return projectRequestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
    }
    
    @Transactional
    public ProjectRequest updateStatus(Long id, String newStatus) {
        ProjectRequest request = getRequestById(id);
        String oldStatus = request.getStatus();
        request.setStatus(newStatus);
        
        if ("REVIEWING".equals(newStatus) || "ACCEPTED".equals(newStatus) || "REJECTED".equals(newStatus)) {
            request.setRespondedAt(LocalDateTime.now());
        }
        
        ProjectRequest updated = projectRequestRepository.save(request);
        
        // Send status update email if status changed
        if (!oldStatus.equals(newStatus)) {
            emailService.sendStatusUpdateEmail(
                request.getClientEmail(),
                request.getClientName(),
                newStatus,
                request.getId()
            );
        }
        
        return updated;
    }
    
    @Transactional
    public ProjectRequest updatePriority(Long id, String priority) {
        ProjectRequest request = getRequestById(id);
        request.setPriority(priority);
        return projectRequestRepository.save(request);
    }
    
    @Transactional
    public ProjectRequest updateAdminNotes(Long id, String notes) {
        ProjectRequest request = getRequestById(id);
        request.setAdminNotes(notes);
        return projectRequestRepository.save(request);
    }
    
    @Transactional
    public RequestResponse addResponse(Long requestId, ResponseRequest responseDto, boolean isAdmin) {
        ProjectRequest request = getRequestById(requestId);
        
        RequestResponse response = RequestResponse.builder()
            .request(request)
            .senderType(isAdmin ? "ADMIN" : "CLIENT")
            .senderName(isAdmin ? "Admin" : request.getClientName())
            .message(responseDto.getMessage())
            .attachments(responseDto.getAttachments())
            .build();
        
        RequestResponse saved = requestResponseRepository.save(response);
        
        // Send notification email
        if (isAdmin) {
            emailService.sendResponseNotification(
                request.getClientEmail(),
                request.getClientName(),
                responseDto.getMessage(),
                request.getId()
            );
        }
        
        return saved;
    }
    
    public List<RequestResponse> getRequestResponses(Long requestId) {
        return requestResponseRepository.findByRequestIdOrderByCreatedAtAsc(requestId);
    }
    
    @Transactional
    public void deleteRequest(Long id) {
        if (!projectRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Request not found");
        }
        projectRequestRepository.deleteById(id);
    }
}