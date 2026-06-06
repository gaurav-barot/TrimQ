package com.trimq.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TrimQ Notification Service - Kafka consumer for notifications.
 * 
 * Responsibilities:
 * - Consume booking events
 * - Consume payment events
 * - Send email notifications (SendGrid/SMTP)
 * - Send SMS notifications (Twilio/MSG91)
 * - Track notification delivery
 */
@SpringBootApplication(scanBasePackages = {"com.trimq.notification", "com.trimq.common"})
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}

