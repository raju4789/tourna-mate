package com.tournament.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TourniManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourniManagementApplication.class, args);
    }
}
