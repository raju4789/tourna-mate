package com.tournament.tourniai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TourniAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourniAiApplication.class, args);
	}

}
