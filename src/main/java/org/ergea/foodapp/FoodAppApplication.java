package org.ergea.foodapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.ergea.foodapp.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableScheduling
public class FoodAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodAppApplication.class, args);
    }

}
