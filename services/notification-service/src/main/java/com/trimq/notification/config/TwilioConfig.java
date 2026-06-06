package com.trimq.notification.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Twilio SMS configuration.
 */
@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
@Slf4j
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String fromNumber;
    private boolean enabled = false;

    @PostConstruct
    public void init() {
        if (enabled && accountSid != null && !accountSid.startsWith("your-")) {
            Twilio.init(accountSid, authToken);
            log.info("Twilio initialized with account: {}...", 
                    accountSid.substring(0, Math.min(10, accountSid.length())));
        } else {
            log.info("Twilio SMS disabled or not configured");
        }
    }
}

