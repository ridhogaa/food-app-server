package org.ergea.orders.kafkadynamic.configuration;

import lombok.Data;
import org.ergea.orders.kafkadynamic.model.CustomKafkaListenerProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "custom.kafka")
public class CustomKafkaListenerProperties {

    private Map<String, CustomKafkaListenerProperty> listeners;
}
