package com.portfolio.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Map;

@Data
public class ProjectRequestDto {
    @NotBlank(message = "Name is required")
    private String clientName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String clientEmail;
    
    private String clientPhone;
    private String companyName;
    
    @NotBlank(message = "Project title is required")
    private String projectTitle;
    
    private String projectType;
    private String budgetRange;
    private String timeline;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private Map<String, Object> requirements;
    private String[] attachments;
}