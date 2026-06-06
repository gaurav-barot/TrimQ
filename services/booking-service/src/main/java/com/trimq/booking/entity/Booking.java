package com.trimq.booking.entity;

import com.trimq.common.entity.BaseEntity;
import com.trimq.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Booking entity - Core booking record.
 * 
 * Stores customer booking information including:
 * - Shop and service details
 * - Date and time slot
 * - Staff assignment
 * - Payment reference
 * - Unique pass code for validation
 */
@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_booking_user_id", columnList = "userId"),
    @Index(name = "idx_booking_shop_id", columnList = "shopId"),
    @Index(name = "idx_booking_shop_date", columnList = "shopId, bookingDate"),
    @Index(name = "idx_booking_pass_code", columnList = "passCode"),
    @Index(name = "idx_booking_status", columnList = "status"),
    @Index(name = "idx_booking_date", columnList = "bookingDate")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who made the booking
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * Shop where booking is made
     */
    @Column(nullable = false)
    private Long shopId;

    /**
     * Service booked
     */
    @Column(nullable = false)
    private Long serviceId;

    /**
     * Service name (denormalized for quick access)
     */
    @Column(nullable = false, length = 100)
    private String serviceName;

    /**
     * Assigned staff (optional)
     */
    private Long staffId;

    /**
     * Staff name (denormalized)
     */
    @Column(length = 100)
    private String staffName;

    /**
     * Booking date
     */
    @Column(nullable = false)
    private LocalDate bookingDate;

    /**
     * Slot start time
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * Slot end time
     */
    @Column(nullable = false)
    private LocalTime endTime;

    /**
     * Duration in minutes
     */
    @Column(nullable = false)
    private Integer durationMinutes;

    /**
     * Service price at time of booking
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Current status of booking
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;

    /**
     * Unique pass code (TQ-YYYYMMDD-XXXX)
     */
    @Column(unique = true, length = 20)
    private String passCode;

    /**
     * QR code image as Base64 (for pass)
     */
    @Column(columnDefinition = "TEXT")
    private String qrCodeBase64;

    /**
     * Razorpay payment order ID
     */
    @Column(length = 50)
    private String paymentOrderId;

    /**
     * Razorpay payment ID (after successful payment)
     */
    @Column(length = 50)
    private String paymentId;

    /**
     * Customer notes/special requests
     */
    @Column(length = 500)
    private String customerNotes;

    /**
     * Cancellation reason (if cancelled)
     */
    @Column(length = 500)
    private String cancellationReason;

    /**
     * User contact details (denormalized for notifications)
     */
    @Column(length = 100)
    private String customerName;

    @Column(length = 15)
    private String customerPhone;

    @Column(length = 100)
    private String customerEmail;

    /**
     * Shop name (denormalized)
     */
    @Column(length = 100)
    private String shopName;

    /**
     * Token number for the day (queue position)
     */
    private Integer tokenNumber;

    /**
     * Check if booking can be cancelled
     */
    public boolean isCancellable() {
        return status == BookingStatus.PENDING || 
               status == BookingStatus.CONFIRMED;
    }

    /**
     * Check if booking is upcoming (not yet started)
     */
    public boolean isUpcoming() {
        LocalDate today = LocalDate.now();
        return (bookingDate.isAfter(today)) ||
               (bookingDate.isEqual(today) && startTime.isAfter(LocalTime.now()));
    }
}

