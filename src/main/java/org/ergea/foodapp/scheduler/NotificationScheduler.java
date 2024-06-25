package org.ergea.foodapp.scheduler;

import org.ergea.foodapp.dto.ProductResponse;
import org.ergea.foodapp.service.ProductService;
import org.ergea.foodapp.serviceimpl.KafkaNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationScheduler {

    @Autowired
    private KafkaNotificationService kafkaNotificationService;

    @Autowired
    private ProductService productService;

    private static final Double PROMO_PRICE_THRESHOLD = 300.0;

    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    @Scheduled(cron = "${cron.expression}")
    public void sendPromoNotification() {
        taskExecutor.execute(() -> {
            List<ProductResponse> products = productService.findByPromo(PROMO_PRICE_THRESHOLD);
            for (ProductResponse product : products) {
                String promoMessage = String.format("Promo: %s at $%.2f order now!", product.getName(), product.getPrice());
                kafkaNotificationService.sendNotification(promoMessage);
            }
        });
    }
}
