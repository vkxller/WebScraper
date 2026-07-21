package org.diegoreyes.webscraper.application;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

public final class ProductScraperService {

    private final HtmlClient htmlClient;
    private final ProductParser productParser;

    public ProductScraperService(
            HtmlClient htmlClient,
            ProductParser productParser
    ) {
        this.htmlClient = Objects.requireNonNull(
                htmlClient,
                "HtmlClient must not be null"
        );

        this.productParser = Objects.requireNonNull(
                productParser,
                "ProductParser must not be null"
        );
    }

    public List<Product> scrape(URI uri) throws IOException {
        Objects.requireNonNull(
                uri,
                "URI must not be null"
        );

        String html = htmlClient.download(uri);
        List<Product> products = productParser.parse(html);

        return List.copyOf(products);
    }
}