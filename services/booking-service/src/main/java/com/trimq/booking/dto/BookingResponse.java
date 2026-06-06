package com.trimq.booking.dto;

import com.trimq.common.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Response DTO for booking details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    
    private Long id;
    private Long userId;
    
    // Shop details
    private Long shopId;
    private String shopName;
    
    // Service details
    private Long serviceId;
    private String serviceName;
    
    // Staff details (optional)
    private Long staffId;
    private String staffName;
    
    // Timing
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    
    // Pricing
    private BigDecimal amount;
    
    // Status
    private BookingStatus status;
    private String statusDescription;
    
    // Pass details
    private String passCode;
    private String qrCodeBase64;
    private Integer tokenNumber;
    
    // Payment
    private String paymentOrderId;
    private String paymentId;
    
    // Customer
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerNotes;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Computed flags
    private boolean cancellable;
    private boolean upcoming;
}

