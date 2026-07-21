package org.diegoreyes.webscraper;

import org.diegoreyes.webscraper.application.ProductScraperService;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.infrastructure.client.JsoupHtmlClient;
import org.diegoreyes.webscraper.infrastructure.parser.FalabellaProductParser;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public final class WebScraperApplication {

    private static final URI FALABELLA_URL = URI.create(
            "https://www.falabella.com/falabella-cl/category/cat40052/Computadores"
    );

    private WebScraperApplication() {
    }

    public static void main(String[] args) {
        HtmlClient htmlClient = new JsoupHtmlClient();
        ProductParser productParser = new FalabellaProductParser();

        ProductScraperService scraperService =
                new ProductScraperService(
                        htmlClient,
                        productParser
                );

        try {
            List<Product> products =
                    scraperService.scrape(FALABELLA_URL);

            printProducts(products);

        } catch (IOException exception) {
            System.err.println(
                    "Unable to download the Falabella page: "
                            + exception.getMessage()
            );
        }
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println(
                    "No products were found."
            );

            return;
        }

        products.forEach(System.out::println);

        System.out.println(
                "Products found: " + products.size()
        );
    }
}