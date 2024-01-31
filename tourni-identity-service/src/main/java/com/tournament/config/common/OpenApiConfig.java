package com.tournament.config.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Tournament Identity Service",
                version = "1.0.0",
                description = "Handles user creation and authentication",
                contact = @Contact(
                        name = "Raju MLN",
                        email = "narasimha4789@gmail.com",
                        url = "https://www.linkedin.com/in/raju-m-l-n/"
                )
        )
)
@Configuration
public class OpenApiConfig {}
