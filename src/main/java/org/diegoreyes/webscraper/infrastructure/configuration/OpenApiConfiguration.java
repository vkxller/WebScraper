package org.diegoreyes.webscraper.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * OpenAPI 3 / Swagger-UI documentation bean configuration.
 * Strictly restricted to the 'dev' Spring profile.
 */
@Configuration
@Profile("dev")
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WebScraper REST API")
                        .version("1.0.0")
                        .description("Microservice REST API for real-time web scraping, search and PostgreSQL persistence of e-commerce products from Falabella Chile.")
                        .contact(new Contact()
                                .name("Diego Reyes")
                                .url("https://github.com/vkxller/WebScraper"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
