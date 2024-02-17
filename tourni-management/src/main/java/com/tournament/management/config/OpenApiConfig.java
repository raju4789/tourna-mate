package com.tournament.management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Tournament Points Table Tracker",
                version = "1.0.0",
                description = "Tournament Points Table Tracker",
                contact = @Contact(
                        name = "Raju MLN",
                        email = "narasimha4789@gmail.com",
                        url = "https://www.linkedin.com/in/raju-m-l-n/"
                )
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production ENV",
                        url = ""
                )
        }
)
@Configuration
public class OpenApiConfig {}
