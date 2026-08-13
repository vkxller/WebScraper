package org.diegoreyes.webscraper.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.diegoreyes.webscraper.application.ProductScraperService;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.domain.repository.ProductRepository;
import org.diegoreyes.webscraper.infrastructure.client.JsoupHtmlClient;
import org.diegoreyes.webscraper.infrastructure.parser.FalabellaProductParser;
import org.diegoreyes.webscraper.infrastructure.repository.InMemoryProductRepository;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ProductApiApplication {

    private static final int PORT = 8080;

    private static final URI DEFAULT_URL = URI.create(
            "https://www.falabella.com/falabella-cl/category/cat40052/Computadores"
    );

    private static final String FALABELLA_SEARCH_BASE =
            "https://www.falabella.com/falabella-cl/search?Ntt=";

    private ProductApiApplication() {
    }

    public static void main(String[] args) throws IOException {
        ProductRepository productRepository =
                new InMemoryProductRepository();

        ProductScraperService scraperService =
                createScraperService(productRepository);

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        server.createContext(
                "/api/products",
                exchange -> handleProducts(
                        exchange,
                        scraperService
                )
        );

        server.start();

        System.out.println(
                "Product API running at http://localhost:" + PORT
        );
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

    private static void handleProducts(
            HttpExchange exchange,
            ProductScraperService scraperService
    ) throws IOException {

        addCorsHeaders(exchange);

        if ("OPTIONS".equalsIgnoreCase(
                exchange.getRequestMethod()
        )) {
            sendResponse(
                    exchange,
                    204,
                    ""
            );
            return;
        }

        if (!"GET".equalsIgnoreCase(
                exchange.getRequestMethod()
        )) {
            sendResponse(
                    exchange,
                    405,
                    "{\"error\":\"Method not allowed\"}"
            );
            return;
        }

        URI targetUri = resolveTargetUri(exchange.getRequestURI());

        try {
            System.out.println("API request for: " + targetUri);
            List<Product> products =
                    scraperService.scrape(
                            targetUri
                    );

            System.out.println("Products scraped: " + products.size());

            sendResponse(
                    exchange,
                    200,
                    productsToJson(products)
            );

        } catch (Exception exception) {
            System.err.println("Scraper error: " + exception.getMessage());
            exception.printStackTrace(System.err);

            String errorMessage = exception.getMessage() != null
                    ? exception.getMessage()
                    : "Unable to obtain products from the scraper.";

            sendResponse(
                    exchange,
                    500,
                    "{\"error\":\"" + escapeJson(errorMessage) + "\"}"
            );
        }
    }

    private static URI resolveTargetUri(URI requestUri) {
        String query = requestUri.getRawQuery();
        if (query == null || query.isBlank()) {
            return DEFAULT_URL;
        }

        for (String param : query.split("&")) {
            int equalsIndex = param.indexOf('=');
            if (equalsIndex > 0) {
                String key = param.substring(0, equalsIndex);
                String rawValue = param.substring(equalsIndex + 1);

                if ("search".equalsIgnoreCase(key) || "q".equalsIgnoreCase(key)) {
                    String decoded = URLDecoder.decode(rawValue, StandardCharsets.UTF_8).trim();
                    if (!decoded.isBlank()) {
                        String encoded = URLEncoder.encode(decoded, StandardCharsets.UTF_8);
                        return URI.create(FALABELLA_SEARCH_BASE + encoded);
                    }
                }
            }
        }

        return DEFAULT_URL;
    }

    private static void addCorsHeaders(
            HttpExchange exchange
    ) {
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );
    }

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String body
    ) throws IOException {

        byte[] response =
                body.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.sendResponseHeaders(
                statusCode,
                response.length
        );

        try (OutputStream outputStream =
                     exchange.getResponseBody()) {

            outputStream.write(response);
        }
    }

    private static String productsToJson(
            List<Product> products
    ) {
        StringBuilder json =
                new StringBuilder("[");

        for (int index = 0;
             index < products.size();
             index++) {

            if (index > 0) {
                json.append(",");
            }

            json.append(
                    productToJson(
                            products.get(index)
                    )
            );
        }

        return json.append("]").toString();
    }

    private static String productToJson(
            Product product
    ) {
        return "{"
                + "\"id\":\""
                + escapeJson(product.getId().value())
                + "\","
                + "\"store\":\""
                + escapeJson(product.getStore())
                + "\","
                + "\"name\":\""
                + escapeJson(product.getName())
                + "\","
                + "\"price\":"
                + product.getPrice()
                + ","
                + "\"previousPrice\":"
                + optionalNumber(
                product.getPreviousPrice()
        )
                + ","
                + "\"discount\":"
                + optionalText(
                product.getDiscount()
        )
                + ","
                + "\"sourceUrl\":"
                + optionalText(
                product.getSourceUrl()
        )
                + ","
                + "\"imageUrl\":"
                + optionalText(
                product.getImageUrl()
        )
                + "}";
    }

    private static String optionalNumber(
            BigDecimal value
    ) {
        return value == null
                ? "null"
                : value.toString();
    }

    private static String optionalText(
            String value
    ) {
        return value == null
                ? "null"
                : "\""
                  + escapeJson(value)
                  + "\"";
    }

    private static String escapeJson(
            String value
    ) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }
}