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
 * Summary DTO for booking list views.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSummaryResponse {
    
    private Long id;
    private String shopName;
    private String serviceName;
    private String staffName;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal amount;
    private BookingStatus status;
    private String passCode;
    private Integer tokenNumber;
    private boolean cancellable;
    private boolean upcoming;
}

