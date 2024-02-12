package com.tournament.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TournamentGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(TournamentGatewayApplication.class, args);
    }
}
