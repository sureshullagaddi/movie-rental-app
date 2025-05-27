package com.etraveligroup.movie.rental.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    /**
     * Configures OpenAPI documentation for the Generate Invoice API.
     * This configuration sets the title, description, version, and contact information for the API.
     *
     * @return OpenAPI instance with the configured information
     * @author Suresh
     * @version 1.0
     * @since 1.0
     */
    @Bean
    public OpenAPI matchmakingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Generate Invoice API")
                        .description("API documentation for generate invoice")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("Support@support.com")));
    }
}