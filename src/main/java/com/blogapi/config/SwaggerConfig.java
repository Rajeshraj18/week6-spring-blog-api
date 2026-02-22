package com.blogapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI blogApiOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Blog Management REST API")
                        .description("Spring Boot 3.x RESTful API for a blog management system")
                        .version("v1.0.0")
                        .contact(new Contact().name("Rajesh")));
    }
}
