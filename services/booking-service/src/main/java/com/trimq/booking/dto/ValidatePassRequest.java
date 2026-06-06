package com.trimq.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for validating a booking pass.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatePassRequest {
    
    @NotBlank(message = "Pass code is required")
    @Pattern(regexp = "^TQ-\\d{8}-[A-Z0-9]{4}$", 
             message = "Invalid pass code format. Expected: TQ-YYYYMMDD-XXXX")
    private String passCode;
}

