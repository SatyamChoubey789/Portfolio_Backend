package com.portfolio.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Map;

@Data
public class BlogPostRequest {
    @NotBlank(message = "Slug is required")
    private String slug;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String excerpt;
    
    @NotNull(message = "Content is required")
    private Map<String, Object> content; // Tiptap JSON
    
    private String coverImageUrl;
    private String[] tags;
    private Integer readTimeMinutes;
    private Boolean featured;
    private String status;
}
