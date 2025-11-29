package com.portfolio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private ProjectRequest request;
    
    @Column(nullable = false, length = 50)
    private String senderType;
    
    private String senderName;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private String[] attachments;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}