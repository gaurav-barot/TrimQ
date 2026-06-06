package com.trimq.booking.dto;

import com.trimq.common.enums.BookingStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for updating a booking (reschedule).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {
    
    private LocalDate bookingDate;
    
    private LocalTime startTime;
    
    private Long staffId;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String customerNotes;
}

