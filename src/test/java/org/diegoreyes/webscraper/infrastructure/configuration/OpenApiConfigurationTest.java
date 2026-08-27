package org.diegoreyes.webscraper.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OpenApiConfiguration unit tests")
class OpenApiConfigurationTest {

    @Test
    @DisplayName("Should create custom OpenAPI bean with title and contact")
    void shouldCreateOpenApiBean() {
        OpenApiConfiguration configuration = new OpenApiConfiguration();
        OpenAPI openAPI = configuration.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("WebScraper REST API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getContact());
        assertEquals("Diego Reyes", openAPI.getInfo().getContact().getName());
    }
}
