package com.trimq.booking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for creating a new booking.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    
    @NotNull(message = "Shop ID is required")
    private Long shopId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    private Long staffId;
    
    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be today or in future")
    private LocalDate bookingDate;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String customerNotes;
}

