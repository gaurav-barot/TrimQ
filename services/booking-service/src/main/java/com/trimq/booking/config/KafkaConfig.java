package com.trimq.booking.config;

import com.trimq.common.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka configuration for booking events.
 */
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic bookingEventsTopic() {
        return TopicBuilder.name(KafkaConstants.TOPIC_BOOKING_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentEventsTopic() {
        return TopicBuilder.name(KafkaConstants.TOPIC_PAYMENT_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}

