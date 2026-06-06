package com.trimq.booking.repository;

import com.trimq.booking.entity.TimeSlot;
import com.trimq.common.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for TimeSlot entity.
 */
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    /**
     * Find slots for shop on date
     */
    List<TimeSlot> findByShopIdAndSlotDateOrderByStartTimeAsc(Long shopId, LocalDate slotDate);

    /**
     * Find available slots for shop on date
     */
    List<TimeSlot> findByShopIdAndSlotDateAndStatusOrderByStartTimeAsc(
            Long shopId, LocalDate slotDate, SlotStatus status);

    /**
     * Find specific slot
     */
    Optional<TimeSlot> findByShopIdAndSlotDateAndStartTime(
            Long shopId, LocalDate slotDate, LocalTime startTime);

    /**
     * Find slot with staff
     */
    Optional<TimeSlot> findByShopIdAndSlotDateAndStartTimeAndStaffId(
            Long shopId, LocalDate slotDate, LocalTime startTime, Long staffId);

    /**
     * Update slot status
     */
    @Modifying
    @Query("UPDATE TimeSlot t SET t.status = :status, t.bookingId = :bookingId " +
           "WHERE t.id = :slotId")
    int updateSlotStatus(
            @Param("slotId") Long slotId,
            @Param("status") SlotStatus status,
            @Param("bookingId") Long bookingId);

    /**
     * Release slot (set to available)
     */
    @Modifying
    @Query("UPDATE TimeSlot t SET t.status = 'AVAILABLE', t.bookingId = null " +
           "WHERE t.bookingId = :bookingId")
    int releaseSlotByBookingId(@Param("bookingId") Long bookingId);

    /**
     * Delete old slots
     */
    @Modifying
    @Query("DELETE FROM TimeSlot t WHERE t.slotDate < :date")
    int deleteOldSlots(@Param("date") LocalDate date);

    /**
     * Check if slots exist for shop on date
     */
    boolean existsByShopIdAndSlotDate(Long shopId, LocalDate slotDate);

    /**
     * Count available slots
     */
    @Query("SELECT COUNT(t) FROM TimeSlot t " +
           "WHERE t.shopId = :shopId AND t.slotDate = :date AND t.status = 'AVAILABLE'")
    int countAvailableSlots(@Param("shopId") Long shopId, @Param("date") LocalDate date);
}

