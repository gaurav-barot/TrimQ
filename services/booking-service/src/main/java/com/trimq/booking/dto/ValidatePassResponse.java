package com.trimq.booking.dto;

import com.trimq.common.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Response DTO for pass validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatePassResponse {
    
    private boolean valid;
    private String message;
    
    // Booking details (only if valid)
    private Long bookingId;
    private String customerName;
    private String customerPhone;
    private String serviceName;
    private String staffName;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer tokenNumber;
    private BigDecimal amount;
    private BookingStatus status;
    private boolean alreadyUsed;
}

