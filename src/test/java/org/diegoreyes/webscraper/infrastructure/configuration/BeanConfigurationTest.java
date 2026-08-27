package org.diegoreyes.webscraper.infrastructure.configuration;

import org.diegoreyes.webscraper.application.ProductScraperService;
import org.diegoreyes.webscraper.application.usecase.DeleteProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.GetAllProductsUseCase;
import org.diegoreyes.webscraper.application.usecase.GetProductByIdUseCase;
import org.diegoreyes.webscraper.application.usecase.ScrapeAndSaveProductsUseCase;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BeanConfiguration unit tests")
class BeanConfigurationTest {

    private final BeanConfiguration configuration = new BeanConfiguration();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private HtmlClient htmlClient;

    @Mock
    private ProductParser productParser;

    @Test
    @DisplayName("Should create infrastructure and use case beans")
    void shouldCreateBeans() {
        assertNotNull(configuration.htmlClient());
        assertNotNull(configuration.productParser());
        assertNotNull(configuration.getAllProductsUseCase(productRepository));
        assertNotNull(configuration.getProductByIdUseCase(productRepository));
        assertNotNull(configuration.deleteProductByIdUseCase(productRepository));
        assertNotNull(configuration.scrapeAndSaveProductsUseCase(htmlClient, productParser, productRepository));
        assertNotNull(configuration.productScraperService(htmlClient, productParser, productRepository));
    }
}
