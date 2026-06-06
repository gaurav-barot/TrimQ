package com.trimq.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * TrimQ Shop Service - Handles shop and service management.
 * 
 * Responsibilities:
 * - Shop CRUD operations
 * - Service management (haircut, beard, etc.)
 * - Staff management
 * - Working hours configuration
 * - Shop search and discovery
 */
@SpringBootApplication(scanBasePackages = {"com.trimq.shop", "com.trimq.common"})
@EnableJpaAuditing
public class ShopServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopServiceApplication.class, args);
    }
}

