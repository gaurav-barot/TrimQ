package com.trimq.payment.repository;

import com.trimq.common.enums.PaymentStatus;
import com.trimq.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Payment entity.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Find by Razorpay order ID
     */
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    /**
     * Find by Razorpay payment ID
     */
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    /**
     * Find by booking ID
     */
    Optional<Payment> findByBookingId(Long bookingId);

    /**
     * Find all payments for a user
     */
    Page<Payment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find all payments for a shop
     */
    Page<Payment> findByShopIdOrderByCreatedAtDesc(Long shopId, Pageable pageable);

    /**
     * Find payments by status
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Find successful payments for shop in date range
     */
    @Query("SELECT p FROM Payment p WHERE p.shopId = :shopId " +
           "AND p.status = 'SUCCESS' " +
           "AND p.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY p.createdAt DESC")
    List<Payment> findSuccessfulPaymentsByShopAndDateRange(
            @Param("shopId") Long shopId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate total revenue for shop in date range
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
           "WHERE p.shopId = :shopId AND p.status = 'SUCCESS' " +
           "AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueByShopAndDateRange(
            @Param("shopId") Long shopId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Count payments by status for shop
     */
    @Query("SELECT p.status, COUNT(p) FROM Payment p " +
           "WHERE p.shopId = :shopId " +
           "GROUP BY p.status")
    List<Object[]> countByShopIdGroupByStatus(@Param("shopId") Long shopId);

    /**
     * Find pending payments older than given time (for cleanup/alerts)
     */
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' " +
           "AND p.createdAt < :cutoff")
    List<Payment> findStalePendingPayments(@Param("cutoff") LocalDateTime cutoff);
}

