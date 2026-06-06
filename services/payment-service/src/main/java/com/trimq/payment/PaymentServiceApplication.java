package com.trimq.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * TrimQ Payment Service - Handles payment processing.
 * 
 * Responsibilities:
 * - Razorpay order creation
 * - Payment verification
 * - Webhook processing
 * - Refund handling
 * - Transaction history
 * - Kafka event publishing
 */
@SpringBootApplication(scanBasePackages = {"com.trimq.payment", "com.trimq.common"})
@EnableJpaAuditing
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}

