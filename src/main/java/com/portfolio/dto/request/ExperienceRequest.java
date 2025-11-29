package com.portfolio.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ExperienceRequest {
    @NotBlank(message = "Company is required")
    private String company;
    
    @NotBlank(message = "Role is required")
    private String role;
    
    private String location;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    private String description;
    private String[] achievements;
    private String[] techStack;
    private String companyLogoUrl;
    private Integer displayOrder;
}