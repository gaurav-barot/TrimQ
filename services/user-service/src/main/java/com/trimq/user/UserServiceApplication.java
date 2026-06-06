package com.trimq.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * TrimQ User Service - Handles authentication and user management.
 * 
 * Responsibilities:
 * - User registration (email + phone)
 * - OTP generation and verification
 * - JWT token generation and refresh
 * - Profile management
 * - Password reset
 */
@SpringBootApplication(scanBasePackages = {"com.trimq.user", "com.trimq.common"})
@EnableJpaAuditing
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

