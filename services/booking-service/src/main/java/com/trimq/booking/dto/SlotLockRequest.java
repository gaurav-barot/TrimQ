package com.trimq.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for locking a slot temporarily.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotLockRequest {
    
    @NotNull(message = "Shop ID is required")
    private Long shopId;
    
    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be today or in future")
    private LocalDate date;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    private Long staffId;
}

