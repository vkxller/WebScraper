package org.diegoreyes.webscraper;

import org.diegoreyes.webscraper.application.ProductScraperService;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.infrastructure.client.JsoupHtmlClient;
import org.diegoreyes.webscraper.infrastructure.parser.FalabellaProductParser;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public final class WebScraperApplication {

    private static final URI FALABELLA_URL =
            URI.create(
                    "https://www.falabella.com/"
                            + "falabella-cl/category/"
                            + "cat40052/Computadores"
            );

    private static final NumberFormat PRICE_FORMAT =
            NumberFormat.getIntegerInstance(
                    Locale.forLanguageTag("es-CL")
            );

    private WebScraperApplication() {
    }

    public static void main(String[] args) {
        HtmlClient htmlClient =
                new JsoupHtmlClient();

        ProductParser productParser =
                new FalabellaProductParser();

        ProductScraperService scraperService =
                new ProductScraperService(
                        htmlClient,
                        productParser
                );

        try {
            List<Product> products =
                    scraperService.scrape(
                            FALABELLA_URL
                    );

            printProducts(products);

        } catch (IOException exception) {
            System.err.println(
                    "Unable to download the Falabella page: "
                            + exception.getMessage()
            );
        }
    }

    private static void printProducts(
            List<Product> products
    ) {
        if (products.isEmpty()) {
            System.out.println(
                    "No products were found."
            );

            return;
        }

        System.out.println(
                "Products found: " + products.size()
        );

        System.out.println();

        for (Product product : products) {
            printProduct(product);
        }
    }

    private static void printProduct(
            Product product
    ) {
        System.out.println(
                "Store: " + product.getStore()
        );

        System.out.println(
                "Name: " + product.getName()
        );

        System.out.println(
                "Price: "
                        + formatPrice(
                        product.getPrice()
                )
        );

        if (product.getPreviousPrice() != null) {
            System.out.println(
                    "Previous price: "
                            + formatPrice(
                            product.getPreviousPrice()
                    )
            );
        } else {
            System.out.println(
                    "Previous price: Not available"
            );
        }

        if (product.getDiscount() != null) {
            System.out.println(
                    "Discount: "
                            + product.getDiscount()
            );
        } else {
            System.out.println(
                    "Discount: Not available"
            );
        }

        if (product.getSourceUrl() != null) {
            System.out.println(
                    "Source URL: "
                            + product.getSourceUrl()
            );
        } else {
            System.out.println(
                    "Source URL: Not available"
            );
        }

        System.out.println(
                "--------------------------------"
        );
    }

    private static String formatPrice(
            BigDecimal price
    ) {
        return "$"
                + PRICE_FORMAT.format(
                price.toBigInteger()
        );
    }
}