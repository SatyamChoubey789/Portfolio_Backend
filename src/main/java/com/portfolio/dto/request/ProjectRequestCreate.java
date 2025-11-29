package com.portfolio.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProjectRequestCreate {
    @NotBlank(message = "Slug is required")
    private String slug;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String subtitle;
    private String description;
    private String coverImageUrl;
    private String[] techStack;
    private String githubUrl;
    private String liveUrl;
    private String category;
    private Boolean featured;
    private Integer displayOrder;
    private String status;
}