package com.trimq.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Email configuration.
 */
@Configuration
@ConfigurationProperties(prefix = "email")
@Data
public class EmailConfig {

    private String from;
    private String fromName;
    private boolean enabled = true;
}

