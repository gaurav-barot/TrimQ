package com.trimq.payment.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Razorpay configuration.
 */
@Configuration
@ConfigurationProperties(prefix = "razorpay")
@Data
@Slf4j
public class RazorpayConfig {

    private String keyId;
    private String keySecret;
    private String webhookSecret;
    private String currency = "INR";
    private String receiptPrefix = "TQ";

    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        log.info("Initializing Razorpay client with key: {}...", 
                keyId.substring(0, Math.min(10, keyId.length())));
        return new RazorpayClient(keyId, keySecret);
    }
}

