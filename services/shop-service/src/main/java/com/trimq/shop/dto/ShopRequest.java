package com.trimq.shop.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating/updating a shop.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopRequest {

    @NotBlank(message = "Shop name is required")
    @Size(min = 2, max = 100, message = "Shop name must be between 2 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Please provide a valid 10-digit Indian mobile number")
    private String phone;

    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String addressLine;

    @NotBlank(message = "Area is required")
    @Size(max = 100, message = "Area cannot exceed 100 characters")
    private String area;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^\\d{6}$", message = "Please provide a valid 6-digit pincode")
    private String pincode;

    private Double latitude;
    private Double longitude;

    private List<String> images;
    private String coverImage;

    @Min(value = 15, message = "Slot duration must be at least 15 minutes")
    @Max(value = 120, message = "Slot duration cannot exceed 120 minutes")
    private Integer slotDurationMinutes;

    @Min(value = 1, message = "Advance booking must be at least 1 day")
    @Max(value = 30, message = "Advance booking cannot exceed 30 days")
    private Integer advanceBookingDays;

    private Integer cancellationHours;
    private Integer cancellationChargePercent;

    private List<WorkingHoursRequest> workingHours;
}

