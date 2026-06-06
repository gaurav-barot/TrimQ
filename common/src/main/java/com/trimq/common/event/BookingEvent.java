package com.trimq.common.event;

import com.trimq.common.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event published when a booking is created, updated, or cancelled.
 * 
 * Used for async communication with notification-service.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BookingEvent extends BaseEvent {

    private Long bookingId;
    private String passCode;
    private Long userId;
    private Long shopId;
    private Long serviceId;
    private Long staffId;
    
    // Customer details for notifications
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Shop details
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    
    // Booking details
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String serviceName;
    private BigDecimal amount;
    private BookingStatus status;
    
    // Staff details
    private String staffName;
    
    // Token/Queue number
    private Integer tokenNumber;
    
    // Cancellation
    private String cancellationReason;

    /**
     * Factory method to create a booking event.
     */
    public static BookingEvent create(String eventType, Long bookingId) {
        BookingEvent event = new BookingEvent();
        event.initializeEvent(eventType, "booking-service");
        event.setBookingId(bookingId);
        return event;
    }
}
