package org.diegoreyes.webscraper.application.usecase;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Use case that orchestrates scraping products from a web page
 * and persisting them into the domain ProductRepository.
 */
public class ScrapeAndSaveProductsUseCase {

    private final HtmlClient htmlClient;
    private final ProductParser productParser;
    private final ProductRepository productRepository;

    public ScrapeAndSaveProductsUseCase(
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
        this.productRepository = Objects.requireNonNull(
                productRepository,
                "ProductRepository must not be null"
        );
    }

    public List<Product> execute(URI uri) throws IOException {
        Objects.requireNonNull(
                uri,
                "URI must not be null"
        );

        String html = htmlClient.download(uri);
        List<Product> products = productParser.parse(html);

        productRepository.saveAll(products);

        return List.copyOf(products);
    }
}
