package com.tournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class TournamentGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(TournamentGatewayApplication.class, args);
    }
}
