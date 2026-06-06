package com.trimq.booking.entity;

import com.trimq.common.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * TimeSlot entity - Represents a time slot for a shop.
 * 
 * Slots are generated dynamically based on shop working hours
 * and stored when a booking is made to prevent conflicts.
 */
@Entity
@Table(name = "time_slots", indexes = {
    @Index(name = "idx_slot_shop_date", columnList = "shopId, slotDate"),
    @Index(name = "idx_slot_shop_date_time", columnList = "shopId, slotDate, startTime"),
    @Index(name = "idx_slot_status", columnList = "status"),
    @Index(name = "idx_slot_staff", columnList = "staffId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Shop this slot belongs to
     */
    @Column(nullable = false)
    private Long shopId;

    /**
     * Date of the slot
     */
    @Column(nullable = false)
    private LocalDate slotDate;

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
     * Current status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private SlotStatus status = SlotStatus.AVAILABLE;

    /**
     * Staff assigned to this slot (optional)
     */
    private Long staffId;

    /**
     * Booking ID if slot is booked
     */
    private Long bookingId;

    /**
     * Check if slot is available
     */
    public boolean isAvailable() {
        return status == SlotStatus.AVAILABLE;
    }

    /**
     * Check if slot is in the past
     */
    public boolean isPast() {
        LocalDate today = LocalDate.now();
        return slotDate.isBefore(today) ||
               (slotDate.isEqual(today) && endTime.isBefore(LocalTime.now()));
    }
}

