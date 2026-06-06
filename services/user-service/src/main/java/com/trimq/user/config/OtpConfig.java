package com.trimq.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OTP configuration properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "otp")
public class OtpConfig {

    private int length = 6;
    private int expiryMinutes = 5;
    private int maxAttempts = 3;
}

