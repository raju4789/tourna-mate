package com.tournament.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TourniDiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourniDiscoveryServerApplication.class, args);
    }
}
