package com.eventhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableKafka
@EnableDiscoveryClient
public class EventNotificatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventNotificatorApplication.class, args);
    }
}
