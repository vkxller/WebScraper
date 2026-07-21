package org.diegoreyes.webscraper.application;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductScraperServiceTest {

    private static final URI URI_TO_SCRAPE =
            URI.create("https://www.falabella.com/category/computadores");

    private static final String HTML = "<html></html>";

    @Mock
    private HtmlClient htmlClient;

    @Mock
    private ProductParser productParser;

    private ProductScraperService service;

    @BeforeEach
    void setUp() {
        service = new ProductScraperService(
                htmlClient,
                productParser
        );
    }

    @Test
    void shouldDownloadParseAndReturnProducts() throws IOException {
        Product product = createProduct();
        List<Product> parsedProducts = List.of(product);

        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(HTML);

        when(productParser.parse(HTML))
                .thenReturn(parsedProducts);

        List<Product> result = service.scrape(URI_TO_SCRAPE);

        assertEquals(parsedProducts, result);

        verify(htmlClient).download(URI_TO_SCRAPE);
        verify(productParser).parse(HTML);
    }

    @Test
    void shouldCallHtmlClientBeforeProductParser() throws IOException {
        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(HTML);

        when(productParser.parse(HTML))
                .thenReturn(List.of());

        service.scrape(URI_TO_SCRAPE);

        InOrder inOrder = inOrder(
                htmlClient,
                productParser
        );

        inOrder.verify(htmlClient)
                .download(URI_TO_SCRAPE);

        inOrder.verify(productParser)
                .parse(HTML);
    }

    @Test
    void shouldReturnImmutableProductList() throws IOException {
        List<Product> mutableProducts = new ArrayList<>();
        mutableProducts.add(createProduct());

        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(HTML);

        when(productParser.parse(HTML))
                .thenReturn(mutableProducts);

        List<Product> result = service.scrape(URI_TO_SCRAPE);

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.add(createAnotherProduct())
        );
    }

    @Test
    void shouldCreateDefensiveCopyOfParsedProducts() throws IOException {
        List<Product> mutableProducts = new ArrayList<>();
        mutableProducts.add(createProduct());

        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(HTML);

        when(productParser.parse(HTML))
                .thenReturn(mutableProducts);

        List<Product> result = service.scrape(URI_TO_SCRAPE);

        mutableProducts.clear();

        assertEquals(1, result.size());
        assertEquals(createProduct(), result.getFirst());
    }

    @Test
    void shouldReturnEmptyImmutableListWhenParserReturnsEmptyList()
            throws IOException {

        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(HTML);

        when(productParser.parse(HTML))
                .thenReturn(List.of());

        List<Product> result = service.scrape(URI_TO_SCRAPE);

        assertTrue(result.isEmpty());

        assertThrows(
                UnsupportedOperationException.class,
                () -> result.add(createProduct())
        );
    }

    @Test
    void shouldPropagateIOExceptionFromHtmlClient() throws IOException {
        IOException expectedException =
                new IOException("Connection failed");

        when(htmlClient.download(URI_TO_SCRAPE))
                .thenThrow(expectedException);

        IOException actualException = assertThrows(
                IOException.class,
                () -> service.scrape(URI_TO_SCRAPE)
        );

        assertSame(expectedException, actualException);

        verify(htmlClient).download(URI_TO_SCRAPE);
        verifyNoInteractions(productParser);
    }

    @Test
    void shouldNotCallParserWhenHtmlDownloadFails() throws IOException {
        when(htmlClient.download(URI_TO_SCRAPE))
                .thenThrow(new IOException("Download failed"));

        assertThrows(
                IOException.class,
                () -> service.scrape(URI_TO_SCRAPE)
        );

        verifyNoInteractions(productParser);
    }

    @Test
    void shouldRejectNullHtmlClient() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ProductScraperService(
                        null,
                        productParser
                )
        );

        assertEquals(
                "HtmlClient must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullProductParser() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ProductScraperService(
                        htmlClient,
                        null
                )
        );

        assertEquals(
                "ProductParser must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullUri() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> service.scrape(null)
        );

        assertEquals(
                "URI must not be null",
                exception.getMessage()
        );

        verifyNoInteractions(
                htmlClient,
                productParser
        );
    }

    @Test
    void shouldRejectNullListReturnedByParser() throws IOException {
        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(HTML);

        when(productParser.parse(HTML))
                .thenReturn(null);

        assertThrows(
                NullPointerException.class,
                () -> service.scrape(URI_TO_SCRAPE)
        );

        verify(htmlClient).download(URI_TO_SCRAPE);
        verify(productParser).parse(HTML);
    }

    @Test
    void shouldPassDownloadedHtmlExactlyToParser() throws IOException {
        String downloadedHtml =
                "<html><body><h1>Products</h1></body></html>";

        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(downloadedHtml);

        when(productParser.parse(downloadedHtml))
                .thenReturn(List.of());

        service.scrape(URI_TO_SCRAPE);

        verify(productParser).parse(downloadedHtml);
    }

    @Test
    void shouldInteractWithDependenciesOnlyOnce() throws IOException {
        when(htmlClient.download(URI_TO_SCRAPE))
                .thenReturn(HTML);

        when(productParser.parse(HTML))
                .thenReturn(List.of());

        service.scrape(URI_TO_SCRAPE);

        verify(htmlClient, times(1))
                .download(URI_TO_SCRAPE);

        verify(productParser, times(1))
                .parse(HTML);

        verifyNoMoreInteractions(
                htmlClient,
                productParser
        );
    }

    private Product createProduct() {
        return new Product(
                "Falabella",
                "Notebook Lenovo",
                new BigDecimal("499990"),
                new BigDecimal("599990"),
                "-17%",
                "https://www.falabella.com/product/123"
        );
    }

    private Product createAnotherProduct() {
        return new Product(
                "Falabella",
                "Notebook HP",
                new BigDecimal("399990"),
                new BigDecimal("449990"),
                "-11%",
                "https://www.falabella.com/product/456"
        );
    }
}