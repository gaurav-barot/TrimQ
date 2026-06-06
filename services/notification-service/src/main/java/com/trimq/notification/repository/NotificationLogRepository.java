package com.trimq.notification.repository;

import com.trimq.common.enums.NotificationType;
import com.trimq.notification.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for NotificationLog entity.
 */
@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    /**
     * Find notifications by booking ID
     */
    List<NotificationLog> findByBookingId(Long bookingId);

    /**
     * Find notifications by user ID
     */
    Page<NotificationLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find failed notifications for retry
     */
    @Query("SELECT n FROM NotificationLog n WHERE n.status = 'FAILED' " +
           "AND n.retryCount < :maxRetries " +
           "AND n.createdAt > :since")
    List<NotificationLog> findFailedNotificationsForRetry(
            @Param("maxRetries") int maxRetries,
            @Param("since") LocalDateTime since);

    /**
     * Check if notification already sent
     */
    boolean existsByBookingIdAndTypeAndChannel(Long bookingId, NotificationType type, 
                                                NotificationLog.Channel channel);

    /**
     * Count notifications by status
     */
    @Query("SELECT n.status, COUNT(n) FROM NotificationLog n " +
           "WHERE n.createdAt > :since " +
           "GROUP BY n.status")
    List<Object[]> countByStatusSince(@Param("since") LocalDateTime since);
}

