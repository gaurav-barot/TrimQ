package com.trimq.notification.entity;

import com.trimq.common.entity.BaseEntity;
import com.trimq.common.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

/**
 * NotificationLog entity - Tracks all sent notifications.
 */
@Entity
@Table(name = "notification_logs", indexes = {
    @Index(name = "idx_notification_user_id", columnList = "userId"),
    @Index(name = "idx_notification_booking_id", columnList = "bookingId"),
    @Index(name = "idx_notification_type", columnList = "type"),
    @Index(name = "idx_notification_channel", columnList = "channel"),
    @Index(name = "idx_notification_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User receiving the notification
     */
    private Long userId;

    /**
     * Related booking (if applicable)
     */
    private Long bookingId;

    /**
     * Related payment (if applicable)
     */
    private Long paymentId;

    /**
     * Notification type
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    /**
     * Channel: EMAIL, SMS
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Channel channel;

    /**
     * Recipient (email or phone)
     */
    @Column(nullable = false, length = 100)
    private String recipient;

    /**
     * Subject (for email)
     */
    @Column(length = 200)
    private String subject;

    /**
     * Message content or template used
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * Delivery status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private Status status = Status.PENDING;

    /**
     * Number of retry attempts
     */
    @Builder.Default
    private Integer retryCount = 0;

    /**
     * External message ID (from provider)
     */
    @Column(length = 100)
    private String externalId;

    /**
     * Error message (if failed)
     */
    @Column(length = 500)
    private String errorMessage;

    /**
     * Notification channels
     */
    public enum Channel {
        EMAIL,
        SMS,
        PUSH,
        WHATSAPP
    }

    /**
     * Delivery status
     */
    public enum Status {
        PENDING,
        SENT,
        DELIVERED,
        FAILED,
        BOUNCED
    }
}

