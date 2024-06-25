package org.ergea.foodapp.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationService {

    private static final String TOPIC = "promo_notifications"; // Your Kafka topic name

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Notification sent to Kafka topic: " + message);
    }
}
