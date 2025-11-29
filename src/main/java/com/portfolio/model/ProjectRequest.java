package com.portfolio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "project_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String clientName;
    
    @Column(nullable = false)
    private String clientEmail;
    
    @Column(length = 50)
    private String clientPhone;
    
    private String companyName;
    
    @Column(nullable = false, length = 500)
    private String projectTitle;
    
    @Column(length = 100)
    private String projectType;
    
    @Column(length = 100)
    private String budgetRange;
    
    @Column(length = 100)
    private String timeline;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> requirements;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private String[] attachments;
    
    @Column(length = 50)
    private String status = "PENDING";
    
    @Column(length = 50)
    private String priority = "NORMAL";
    
    @Column(columnDefinition = "TEXT")
    private String adminNotes;
    
    private LocalDateTime respondedAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<RequestResponse> responses = new ArrayList<>();
}