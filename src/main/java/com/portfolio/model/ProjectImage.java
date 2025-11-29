package com.portfolio.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @Column(nullable = false, length = 500)
    private String imageUrl;
    
    @Column(columnDefinition = "TEXT")
    private String caption;
    
    private Integer displayOrder = 0;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
