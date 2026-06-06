package com.trimq.payment.repository;

import com.trimq.payment.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Transaction entity.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transactions for a payment
     */
    List<Transaction> findByPaymentIdOrderByCreatedAtDesc(Long paymentId);

    /**
     * Find transactions for a shop
     */
    Page<Transaction> findByShopIdOrderByCreatedAtDesc(Long shopId, Pageable pageable);

    /**
     * Find transactions by type
     */
    List<Transaction> findByType(Transaction.TransactionType type);

    /**
     * Find transactions for shop in date range
     */
    @Query("SELECT t FROM Transaction t WHERE t.shopId = :shopId " +
           "AND t.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findByShopIdAndDateRange(
            @Param("shopId") Long shopId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}

