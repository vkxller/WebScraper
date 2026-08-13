package org.diegoreyes.webscraper;

import org.diegoreyes.webscraper.application.ProductScraperService;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.infrastructure.client.JsoupHtmlClient;
import org.diegoreyes.webscraper.infrastructure.parser.FalabellaProductParser;
import org.diegoreyes.webscraper.infrastructure.repository.InMemoryProductRepository;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public final class WebScraperApplication {

    private static final URI DEFAULT_URL = URI.create(
            "https://www.falabella.com/falabella-cl/category/cat40052/Computadores"
    );

    private static final String FALABELLA_SEARCH_BASE =
            "https://www.falabella.com/falabella-cl/search?Ntt=";

    private static final NumberFormat CHILEAN_PRICE_FORMAT =
            NumberFormat.getIntegerInstance(
                    Locale.forLanguageTag("es-CL")
            );

    private WebScraperApplication() {
    }

    public static void main(String[] args) {
        ProductRepository productRepository =
                new InMemoryProductRepository();

        ProductScraperService scraperService =
                createScraperService(productRepository);

        URI targetUri = resolveTargetUri(args);

        try {
            System.out.println("Searching products at: " + targetUri);
            List<Product> products =
                    scraperService.scrape(targetUri);

            printProducts(products);

        } catch (IOException exception) {
            printDownloadError(exception);
        }
    }

    private static URI resolveTargetUri(String[] args) {
        if (args == null || args.length == 0 || args[0].isBlank()) {
            return DEFAULT_URL;
        }

        String searchTerm = String.join(" ", args).trim();
        String encodedTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        return URI.create(FALABELLA_SEARCH_BASE + encodedTerm);
    }

    private static ProductScraperService createScraperService(
            ProductRepository productRepository
    ) {
        HtmlClient htmlClient =
                new JsoupHtmlClient();

        ProductParser productParser =
                new FalabellaProductParser();

        return new ProductScraperService(
                htmlClient,
                productParser,
                productRepository
        );
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println(
                    "No products were found."
            );
            return;
        }

        products.forEach(
                WebScraperApplication::printProduct
        );

        System.out.println();
        System.out.println(
                "Products found: " + products.size()
        );
    }

    private static void printProduct(Product product) {
        System.out.println(
                "ID: " + product.getId()
        );

        System.out.println(
                "Store: " + product.getStore()
        );

        System.out.println(
                "Name: " + product.getName()
        );

        System.out.println(
                "Price: "
                        + formatPrice(product.getPrice())
        );

        System.out.println(
                "Previous price: "
                        + formatOptionalPrice(
                        product.getPreviousPrice()
                )
        );

        System.out.println(
                "Discount: "
                        + formatOptionalText(
                        product.getDiscount()
                )
        );

        System.out.println(
                "Source URL: "
                        + formatOptionalText(
                        product.getSourceUrl()
                )
        );

        System.out.println(
                "Image URL: "
                        + formatOptionalText(
                        product.getImageUrl()
                )
        );

        System.out.println(
                "----------------------------------------"
        );
    }

    private static String formatPrice(BigDecimal price) {
        return "$"
                + CHILEAN_PRICE_FORMAT.format(price);
    }

    private static String formatOptionalPrice(
            BigDecimal price
    ) {
        if (price == null) {
            return "Not available";
        }

        return formatPrice(price);
    }

    private static String formatOptionalText(
            String value
    ) {
        if (value == null || value.isBlank()) {
            return "Not available";
        }

        return value;
    }

    private static void printDownloadError(
            IOException exception
    ) {
        System.err.println(
                "Unable to download the Falabella page: "
                        + exception.getMessage()
        );
    }
}