package org.diegoreyes.webscraper.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.diegoreyes.webscraper.application.ProductScraperService;
import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.infrastructure.client.JsoupHtmlClient;
import org.diegoreyes.webscraper.infrastructure.parser.FalabellaProductParser;
import org.diegoreyes.webscraper.port.HtmlClient;
import org.diegoreyes.webscraper.port.ProductParser;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ProductApiApplication {

    private static final int PORT = 8080;

    private static final URI FALABELLA_URL = URI.create(
            "https://www.falabella.com/falabella-cl/category/cat40052/Computadores"
    );

    private ProductApiApplication() {
    }

    public static void main(String[] args) throws IOException {
        ProductScraperService scraperService =
                createScraperService();

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

    private static ProductScraperService createScraperService() {
        HtmlClient htmlClient =
                new JsoupHtmlClient();

        ProductParser productParser =
                new FalabellaProductParser();

        return new ProductScraperService(
                htmlClient,
                productParser
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

        try {
            List<Product> products =
                    scraperService.scrape(
                            FALABELLA_URL
                    );

            sendResponse(
                    exchange,
                    200,
                    productsToJson(products)
            );

        } catch (IOException exception) {
            sendResponse(
                    exchange,
                    500,
                    "{\"error\":\"Unable to obtain products from the scraper.\"}"
            );
        }
    }

    private static void addCorsHeaders(
            HttpExchange exchange
    ) {
        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "http://localhost:5173"
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