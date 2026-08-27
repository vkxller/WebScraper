package org.diegoreyes.webscraper.application;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Application service that coordinates scraping operations and repository persistence.
 * Uses constructor injection to decouple application logic from infrastructure implementations.
 */
public class ProductScraperService {

    private final HtmlClient htmlClient;
    private final ProductParser productParser;
    private final ProductRepository productRepository;

    public ProductScraperService(
            HtmlClient htmlClient,
            ProductParser productParser,
            ProductRepository productRepository
    ) {
        this.htmlClient = Objects.requireNonNull(
                htmlClient,
                "HtmlClient must not be null"
        );

        this.productParser = Objects.requireNonNull(
                productParser,
                "ProductParser must not be null"
        );

        this.productRepository = productRepository;
    }

    public ProductScraperService(
            HtmlClient htmlClient,
            ProductParser productParser
    ) {
        this(
                htmlClient,
                productParser,
                null
        );
    }

    public List<Product> scrape(URI uri) throws IOException {
        Objects.requireNonNull(
                uri,
                "URI must not be null"
        );

        String html = htmlClient.download(uri);
        List<Product> products = productParser.parse(html);

        if (productRepository != null) {
            productRepository.saveAll(products);
        }

        return List.copyOf(products);
    }

    public List<Product> getStoredProducts() {
        if (productRepository == null) {
            return List.of();
        }
        return productRepository.findAll();
    }
}