package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScrapeAndSaveProductsUseCaseTest {

    private static final URI URI_TO_SCRAPE =
            URI.create("https://www.falabella.com/category/computadores");

    private static final String HTML = "<html><body>products</body></html>";

    @Mock
    private HtmlClient htmlClient;

    @Mock
    private ProductParser productParser;

    @Mock
    private ProductRepository productRepository;

    private ScrapeAndSaveProductsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ScrapeAndSaveProductsUseCase(
                htmlClient,
                productParser,
                productRepository
        );
    }

    @Test
    void shouldScrapeAndSaveProducts() throws IOException {
        Product product = createProduct();
        List<Product> products = List.of(product);

        when(htmlClient.download(URI_TO_SCRAPE)).thenReturn(HTML);
        when(productParser.parse(HTML)).thenReturn(products);

        List<Product> result = useCase.execute(URI_TO_SCRAPE);

        assertEquals(products, result);
        verify(htmlClient).download(URI_TO_SCRAPE);
        verify(productParser).parse(HTML);
        verify(productRepository).saveAll(products);
    }

    @Test
    void shouldRejectNullHtmlClient() {
        assertThrows(
                NullPointerException.class,
                () -> new ScrapeAndSaveProductsUseCase(null, productParser, productRepository)
        );
    }

    @Test
    void shouldRejectNullProductParser() {
        assertThrows(
                NullPointerException.class,
                () -> new ScrapeAndSaveProductsUseCase(htmlClient, null, productRepository)
        );
    }

    @Test
    void shouldRejectNullProductRepository() {
        assertThrows(
                NullPointerException.class,
                () -> new ScrapeAndSaveProductsUseCase(htmlClient, productParser, null)
        );
    }

    @Test
    void shouldRejectNullUri() {
        assertThrows(
                NullPointerException.class,
                () -> useCase.execute(null)
        );
    }

    private Product createProduct() {
        return new Product(
                ProductId.of("prod-1"),
                "Falabella",
                "Notebook Lenovo",
                new BigDecimal("499990"),
                new BigDecimal("599990"),
                "-17%",
                "https://www.falabella.com/product/123",
                null
        );
    }
}
