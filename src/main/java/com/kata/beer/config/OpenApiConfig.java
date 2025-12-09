package com.kata.beer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Beer API - Kata API de Cervezas")
                        .version("1.0.0")
                        .description("""
                                API REST para gestión de cervezas, cervecerías, categorías y estilos.
                                
                                Esta API implementa operaciones CRUD completas y sigue las mejores prácticas REST:
                                - ✅ Verbos HTTP apropiados (GET, POST, PUT, PATCH, DELETE)
                                - ✅ Códigos de estado HTTP correctos
                                - ✅ Manejo robusto de errores y validaciones
                                - ✅ Relaciones entre entidades bien definidas
                                - ✅ Documentación interactiva con Swagger
                                - ✅ Tests unitarios completos
                                
                                Desarrollado con Spring Boot 3.2, Java 17, MySQL y siguiendo principios SOLID.
                                """)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@beerapi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo")
                ));
    }
}

