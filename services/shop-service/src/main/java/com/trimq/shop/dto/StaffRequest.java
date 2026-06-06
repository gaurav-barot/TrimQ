package com.trimq.shop.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating/updating staff.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequest {

    @NotBlank(message = "Staff name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Please provide a valid 10-digit Indian mobile number")
    private String phone;

    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Role is required")
    private String role; // Stylist, Senior Stylist, Junior, etc.

    private List<String> specializations; // ["Haircut", "Beard", "Fade"]

    private String profileImageUrl;
    private Boolean isAvailable;
    private Integer displayOrder;
}

