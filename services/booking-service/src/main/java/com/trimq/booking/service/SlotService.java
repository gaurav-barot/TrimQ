package com.trimq.booking.service;

import com.trimq.booking.config.BookingConfig;
import com.trimq.booking.dto.DaySlotResponse;
import com.trimq.booking.dto.SlotLockRequest;
import com.trimq.booking.dto.SlotLockResponse;
import com.trimq.booking.dto.SlotResponse;
import com.trimq.booking.entity.TimeSlot;
import com.trimq.booking.repository.TimeSlotRepository;
import com.trimq.common.enums.SlotStatus;
import com.trimq.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Service for managing time slots.
 * 
 * Handles slot generation, availability checking, and locking.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SlotService {

    private final TimeSlotRepository slotRepository;
    private final SlotLockService slotLockService;
    private final BookingConfig bookingConfig;

    /**
     * Get available slots for a shop on a specific date.
     * 
     * @param shopId Shop ID
     * @param date Date to check
     * @param durationMinutes Service duration
     * @return Day slot response with all slots
     */
    @Transactional(readOnly = true)
    public DaySlotResponse getAvailableSlots(Long shopId, LocalDate date, Integer durationMinutes) {
        validateDate(date);

        // Generate slots if not exists
        if (!slotRepository.existsByShopIdAndSlotDate(shopId, date)) {
            generateSlotsForDate(shopId, date, durationMinutes);
        }

        List<TimeSlot> slots = slotRepository.findByShopIdAndSlotDateOrderByStartTimeAsc(shopId, date);
        
        List<SlotResponse> slotResponses = slots.stream()
                .filter(slot -> !slot.isPast()) // Filter out past slots
                .map(this::toSlotResponse)
                .toList();

        int availableCount = (int) slotResponses.stream()
                .filter(SlotResponse::isAvailable)
                .count();

        return DaySlotResponse.builder()
                .shopId(shopId)
                .date(date)
                .dayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .shopOpen(true) // TODO: Check shop working hours
                .slots(slotResponses)
                .totalSlots(slotResponses.size())
                .availableSlots(availableCount)
                .build();
    }

    /**
     * Get slots for multiple days (for calendar view).
     * 
     * @param shopId Shop ID
     * @param startDate Start date
     * @param days Number of days
     * @param durationMinutes Service duration
     * @return List of day slot responses
     */
    @Transactional(readOnly = true)
    public List<DaySlotResponse> getAvailableSlotsForDays(Long shopId, LocalDate startDate, 
                                                           int days, Integer durationMinutes) {
        List<DaySlotResponse> result = new ArrayList<>();
        
        for (int i = 0; i < days && i <= bookingConfig.getMaxAdvanceDays(); i++) {
            LocalDate date = startDate.plusDays(i);
            result.add(getAvailableSlots(shopId, date, durationMinutes));
        }
        
        return result;
    }

    /**
     * Lock a slot temporarily before payment.
     * 
     * @param request Lock request
     * @param userId User ID
     * @return Lock response
     */
    public SlotLockResponse lockSlot(SlotLockRequest request, Long userId) {
        validateDate(request.getDate());

        // Check if slot is already locked
        if (slotLockService.isLocked(request.getShopId(), request.getDate(), request.getStartTime())) {
            // Check if locked by same user
            if (slotLockService.isLockedByUser(request.getShopId(), request.getDate(), 
                    request.getStartTime(), userId)) {
                long ttl = slotLockService.getRemainingTtl(request.getShopId(), 
                        request.getDate(), request.getStartTime());
                
                return SlotLockResponse.builder()
                        .locked(true)
                        .message("Slot already locked by you")
                        .expiresAt(java.time.LocalDateTime.now().plusSeconds(ttl))
                        .build();
            }
            
            return SlotLockResponse.builder()
                    .locked(false)
                    .message("Slot is already locked by another user. Please try a different slot.")
                    .build();
        }

        // Try to acquire lock
        String lockId = slotLockService.tryLock(request.getShopId(), request.getDate(), 
                request.getStartTime(), userId);

        if (lockId != null) {
            return SlotLockResponse.builder()
                    .locked(true)
                    .lockId(lockId)
                    .message("Slot locked successfully. Complete payment within 5 minutes.")
                    .expiresAt(java.time.LocalDateTime.now().plusSeconds(bookingConfig.getSlotLockTtlSeconds()))
                    .build();
        }

        return SlotLockResponse.builder()
                .locked(false)
                .message("Failed to lock slot. Please try again.")
                .build();
    }

    /**
     * Release a slot lock.
     * 
     * @param shopId Shop ID
     * @param date Date
     * @param startTime Start time
     * @param lockId Lock ID
     * @return true if released
     */
    public boolean releaseSlotLock(Long shopId, LocalDate date, LocalTime startTime, String lockId) {
        return slotLockService.releaseLock(shopId, date, startTime, lockId);
    }

    /**
     * Check if a slot is available.
     * 
     * @param shopId Shop ID
     * @param date Date
     * @param startTime Start time
     * @return true if available
     */
    @Transactional(readOnly = true)
    public boolean isSlotAvailable(Long shopId, LocalDate date, LocalTime startTime) {
        return slotRepository.findByShopIdAndSlotDateAndStartTime(shopId, date, startTime)
                .map(TimeSlot::isAvailable)
                .orElse(true); // If slot doesn't exist, it can be created
    }

    /**
     * Book a slot (mark as booked).
     * 
     * @param shopId Shop ID
     * @param date Date
     * @param startTime Start time
     * @param endTime End time
     * @param bookingId Booking ID
     * @param staffId Staff ID (optional)
     */
    @Transactional
    public void bookSlot(Long shopId, LocalDate date, LocalTime startTime, 
                         LocalTime endTime, Long bookingId, Long staffId) {
        TimeSlot slot = slotRepository.findByShopIdAndSlotDateAndStartTime(shopId, date, startTime)
                .orElseGet(() -> createSlot(shopId, date, startTime, endTime, staffId));

        slot.setStatus(SlotStatus.BOOKED);
        slot.setBookingId(bookingId);
        slot.setStaffId(staffId);
        slotRepository.save(slot);

        log.info("Slot booked: shop={}, date={}, time={}, booking={}", 
                shopId, date, startTime, bookingId);
    }

    /**
     * Release a booked slot (cancel booking).
     * 
     * @param bookingId Booking ID
     */
    @Transactional
    public void releaseBookedSlot(Long bookingId) {
        int updated = slotRepository.releaseSlotByBookingId(bookingId);
        if (updated > 0) {
            log.info("Slot released for booking: {}", bookingId);
        }
    }

    /**
     * Generate slots for a date based on shop working hours.
     * 
     * TODO: Integrate with Shop Service to get actual working hours.
     */
    @Transactional
    public void generateSlotsForDate(Long shopId, LocalDate date, Integer durationMinutes) {
        if (slotRepository.existsByShopIdAndSlotDate(shopId, date)) {
            return;
        }

        int duration = durationMinutes != null ? durationMinutes : bookingConfig.getDefaultSlotDurationMinutes();
        
        // Default working hours: 9 AM to 9 PM
        // TODO: Get from Shop Service
        LocalTime openTime = LocalTime.of(9, 0);
        LocalTime closeTime = LocalTime.of(21, 0);
        LocalTime breakStart = LocalTime.of(13, 0); // 1 PM
        LocalTime breakEnd = LocalTime.of(14, 0);   // 2 PM

        List<TimeSlot> slots = new ArrayList<>();
        LocalTime current = openTime;

        while (current.plusMinutes(duration).isBefore(closeTime) || 
               current.plusMinutes(duration).equals(closeTime)) {
            
            // Skip break time
            if (!current.isBefore(breakStart) && current.isBefore(breakEnd)) {
                current = breakEnd;
                continue;
            }

            TimeSlot slot = TimeSlot.builder()
                    .shopId(shopId)
                    .slotDate(date)
                    .startTime(current)
                    .endTime(current.plusMinutes(duration))
                    .durationMinutes(duration)
                    .status(SlotStatus.AVAILABLE)
                    .build();
            
            slots.add(slot);
            current = current.plusMinutes(duration);
        }

        slotRepository.saveAll(slots);
        log.info("Generated {} slots for shop {} on {}", slots.size(), shopId, date);
    }

    private TimeSlot createSlot(Long shopId, LocalDate date, LocalTime startTime, 
                                LocalTime endTime, Long staffId) {
        int duration = (int) java.time.Duration.between(startTime, endTime).toMinutes();
        
        return TimeSlot.builder()
                .shopId(shopId)
                .slotDate(date)
                .startTime(startTime)
                .endTime(endTime)
                .durationMinutes(duration)
                .status(SlotStatus.AVAILABLE)
                .staffId(staffId)
                .build();
    }

    private void validateDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(bookingConfig.getMaxAdvanceDays());

        if (date.isBefore(today)) {
            throw new BadRequestException("Cannot book for past dates");
        }

        if (date.isAfter(maxDate)) {
            throw new BadRequestException("Cannot book more than " + 
                    bookingConfig.getMaxAdvanceDays() + " days in advance");
        }
    }

    private SlotResponse toSlotResponse(TimeSlot slot) {
        // Check if slot is locked in Redis
        boolean isLocked = slotLockService.isLocked(slot.getShopId(), slot.getSlotDate(), slot.getStartTime());
        
        boolean available = slot.isAvailable() && !isLocked;
        
        return SlotResponse.builder()
                .id(slot.getId())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .durationMinutes(slot.getDurationMinutes())
                .status(isLocked ? SlotStatus.LOCKED : slot.getStatus())
                .staffId(slot.getStaffId())
                .available(available)
                .build();
    }
}

