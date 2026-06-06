package com.trimq.booking.repository;

import com.trimq.booking.entity.Booking;
import com.trimq.common.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find booking by pass code
     */
    Optional<Booking> findByPassCode(String passCode);

    /**
     * Find all bookings for a user
     */
    Page<Booking> findByUserIdOrderByBookingDateDescStartTimeDesc(Long userId, Pageable pageable);

    /**
     * Find upcoming bookings for a user
     */
    @Query("SELECT b FROM Booking b WHERE b.userId = :userId " +
           "AND (b.bookingDate > :today OR (b.bookingDate = :today AND b.startTime > :now)) " +
           "AND b.status IN ('PENDING', 'CONFIRMED') " +
           "ORDER BY b.bookingDate ASC, b.startTime ASC")
    List<Booking> findUpcomingBookingsByUser(
            @Param("userId") Long userId,
            @Param("today") LocalDate today,
            @Param("now") LocalTime now);

    /**
     * Find bookings for a shop on a specific date
     */
    List<Booking> findByShopIdAndBookingDateOrderByStartTimeAsc(Long shopId, LocalDate bookingDate);

    /**
     * Find all bookings for a shop
     */
    Page<Booking> findByShopIdOrderByBookingDateDescStartTimeDesc(Long shopId, Pageable pageable);

    /**
     * Find bookings by shop and date range
     */
    @Query("SELECT b FROM Booking b WHERE b.shopId = :shopId " +
           "AND b.bookingDate BETWEEN :startDate AND :endDate " +
           "ORDER BY b.bookingDate ASC, b.startTime ASC")
    List<Booking> findByShopIdAndDateRange(
            @Param("shopId") Long shopId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Find today's bookings for a shop by status
     */
    @Query("SELECT b FROM Booking b WHERE b.shopId = :shopId " +
           "AND b.bookingDate = :date AND b.status IN :statuses " +
           "ORDER BY b.startTime ASC")
    List<Booking> findByShopIdAndDateAndStatuses(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date,
            @Param("statuses") List<BookingStatus> statuses);

    /**
     * Check for conflicting booking
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.shopId = :shopId " +
           "AND b.bookingDate = :date AND b.staffId = :staffId " +
           "AND b.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((b.startTime <= :startTime AND b.endTime > :startTime) " +
           "OR (b.startTime < :endTime AND b.endTime >= :endTime) " +
           "OR (b.startTime >= :startTime AND b.endTime <= :endTime))")
    boolean existsConflictingBooking(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date,
            @Param("staffId") Long staffId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * Check for conflicting booking without staff
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.shopId = :shopId " +
           "AND b.bookingDate = :date " +
           "AND b.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((b.startTime <= :startTime AND b.endTime > :startTime) " +
           "OR (b.startTime < :endTime AND b.endTime >= :endTime) " +
           "OR (b.startTime >= :startTime AND b.endTime <= :endTime))")
    boolean existsConflictingBookingWithoutStaff(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * Count bookings by status for shop on date
     */
    @Query("SELECT b.status, COUNT(b) FROM Booking b " +
           "WHERE b.shopId = :shopId AND b.bookingDate = :date " +
           "GROUP BY b.status")
    List<Object[]> countByShopIdAndDateGroupByStatus(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date);

    /**
     * Get max token number for shop on date
     */
    @Query("SELECT COALESCE(MAX(b.tokenNumber), 0) FROM Booking b " +
           "WHERE b.shopId = :shopId AND b.bookingDate = :date")
    int getMaxTokenNumberForShopAndDate(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date);

    /**
     * Find next pending booking for shop after current time
     */
    @Query("SELECT b FROM Booking b WHERE b.shopId = :shopId " +
           "AND b.bookingDate = :date AND b.status = 'CONFIRMED' " +
           "AND b.startTime > :now " +
           "ORDER BY b.startTime ASC LIMIT 1")
    Optional<Booking> findNextBookingForShop(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date,
            @Param("now") LocalTime now);

    /**
     * Find current booking for shop (started but not completed)
     */
    @Query("SELECT b FROM Booking b WHERE b.shopId = :shopId " +
           "AND b.bookingDate = :date AND b.status = 'IN_PROGRESS' " +
           "ORDER BY b.startTime ASC LIMIT 1")
    Optional<Booking> findCurrentBookingForShop(
            @Param("shopId") Long shopId,
            @Param("date") LocalDate date);
}

