package com.kata.beer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Beer API",
        version = "1.0",
        description = "API REST para la gestión de cervezas, cervecerías, categorías y estilos",
        contact = @Contact(
            name = "Kata API Team",
            url = "https://github.com"
        )
    )
)
public class BeerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeerApiApplication.class, args);
    }
}

