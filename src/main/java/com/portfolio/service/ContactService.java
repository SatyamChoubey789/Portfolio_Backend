package com.portfolio.service;

import com.portfolio.model.ContactMessage;
import com.portfolio.repository.ContactMessageRepository;
import com.portfolio.dto.request.ContactRequest;
import com.portfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    
    private final ContactMessageRepository contactMessageRepository;
    private final EmailService emailService;
    
    @Transactional
    public ContactMessage createMessage(ContactRequest request) {
        ContactMessage message = ContactMessage.builder()
            .name(request.getName())
            .email(request.getEmail())
            .subject(request.getSubject())
            .message(request.getMessage())
            .status("UNREAD")
            .build();
        
        ContactMessage saved = contactMessageRepository.save(message);
        
        // Send notification to admin
        emailService.sendContactMessageNotification(
            saved.getName(),
            saved.getEmail(),
            saved.getMessage()
        );
        
        return saved;
    }
    
    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public List<ContactMessage> getMessagesByStatus(String status) {
        return contactMessageRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    @Transactional
    public ContactMessage updateStatus(Long id, String status) {
        ContactMessage message = contactMessageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        
        message.setStatus(status);
        return contactMessageRepository.save(message);
    }
    
    @Transactional
    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }
}