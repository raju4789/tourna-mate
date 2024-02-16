package com.tournament.tournimanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TourniManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourniManagerApplication.class, args);
	}

}
