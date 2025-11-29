package com.portfolio.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResponseRequest {
    @NotBlank(message = "Message is required")
    private String message;
    
    private String[] attachments;
}

