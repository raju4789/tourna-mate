package com.tournament.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class TourniManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourniManagementApplication.class, args);
    }
}
