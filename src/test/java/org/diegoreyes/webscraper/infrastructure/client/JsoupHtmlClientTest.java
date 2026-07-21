package org.diegoreyes.webscraper.infrastructure.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class JsoupHtmlClientTest {

    private static final String USER_AGENT =
            "WebScraper-Test/1.0";

    private static final int TIMEOUT_MILLISECONDS =
            2_000;

    private HttpServer server;
    private ExecutorService executor;
    private JsoupHtmlClient htmlClient;
    private URI baseUri;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(
                new InetSocketAddress("localhost", 0),
                0
        );

        executor = Executors.newCachedThreadPool();
        server.setExecutor(executor);
        server.start();

        int port = server.getAddress().getPort();

        baseUri = URI.create(
                "http://localhost:" + port
        );

        htmlClient = new JsoupHtmlClient(
                USER_AGENT,
                TIMEOUT_MILLISECONDS
        );
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }

        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @Test
    void shouldDownloadHtml() throws IOException {
        String expectedHtml = """
                <html>
                    <body>
                        <h1>Falabella products</h1>
                    </body>
                </html>
                """;

        server.createContext(
                "/products",
                exchange -> sendResponse(
                        exchange,
                        200,
                        expectedHtml
                )
        );

        String result = htmlClient.download(
                resolve("/products")
        );

        assertEquals(
                expectedHtml,
                result
        );
    }

    @Test
    void shouldDownloadHtmlUsingDefaultConstructor()
            throws IOException {

        String expectedHtml = """
                <html>
                    <body>Products</body>
                </html>
                """;

        server.createContext(
                "/default-client",
                exchange -> sendResponse(
                        exchange,
                        200,
                        expectedHtml
                )
        );

        JsoupHtmlClient defaultClient =
                new JsoupHtmlClient();

        String result = defaultClient.download(
                resolve("/default-client")
        );

        assertEquals(
                expectedHtml,
                result
        );
    }

    @Test
    void shouldSendConfiguredUserAgent() throws IOException {
        AtomicReference<String> receivedUserAgent =
                new AtomicReference<>();

        server.createContext(
                "/user-agent",
                exchange -> {
                    receivedUserAgent.set(
                            exchange.getRequestHeaders()
                                    .getFirst("User-Agent")
                    );

                    sendResponse(
                            exchange,
                            200,
                            "<html></html>"
                    );
                }
        );

        htmlClient.download(
                resolve("/user-agent")
        );

        assertEquals(
                USER_AGENT,
                receivedUserAgent.get()
        );
    }

    @Test
    void shouldTrimConfiguredUserAgent() throws IOException {
        AtomicReference<String> receivedUserAgent =
                new AtomicReference<>();

        server.createContext(
                "/trimmed-user-agent",
                exchange -> {
                    receivedUserAgent.set(
                            exchange.getRequestHeaders()
                                    .getFirst("User-Agent")
                    );

                    sendResponse(
                            exchange,
                            200,
                            "<html></html>"
                    );
                }
        );

        JsoupHtmlClient client = new JsoupHtmlClient(
                "  WebScraper-Test/2.0  ",
                TIMEOUT_MILLISECONDS
        );

        client.download(
                resolve("/trimmed-user-agent")
        );

        assertEquals(
                "WebScraper-Test/2.0",
                receivedUserAgent.get()
        );
    }

    @Test
    void shouldFollowRedirects() throws IOException {
        String expectedHtml = """
                <html>
                    <body>Redirect destination</body>
                </html>
                """;

        server.createContext(
                "/redirect",
                exchange -> {
                    exchange.getResponseHeaders().add(
                            "Location",
                            "/destination"
                    );

                    exchange.sendResponseHeaders(
                            302,
                            -1
                    );

                    exchange.close();
                }
        );

        server.createContext(
                "/destination",
                exchange -> sendResponse(
                        exchange,
                        200,
                        expectedHtml
                )
        );

        String result = htmlClient.download(
                resolve("/redirect")
        );

        assertEquals(
                expectedHtml,
                result
        );
    }

    @Test
    void shouldThrowExceptionWhenServerReturns404() {
        server.createContext(
                "/not-found",
                exchange -> sendResponse(
                        exchange,
                        404,
                        "Page not found"
                )
        );

        HttpStatusException exception = assertThrows(
                HttpStatusException.class,
                () -> htmlClient.download(
                        resolve("/not-found")
                )
        );

        assertEquals(
                404,
                exception.getStatusCode()
        );
    }

    @Test
    void shouldThrowExceptionWhenServerReturns500() {
        server.createContext(
                "/server-error",
                exchange -> sendResponse(
                        exchange,
                        500,
                        "Internal server error"
                )
        );

        HttpStatusException exception = assertThrows(
                HttpStatusException.class,
                () -> htmlClient.download(
                        resolve("/server-error")
                )
        );

        assertEquals(
                500,
                exception.getStatusCode()
        );
    }

    @Test
    void shouldThrowIOExceptionWhenRequestExceedsTimeout() {
        server.createContext(
                "/slow-response",
                exchange -> {
                    try {
                        Thread.sleep(500);

                        sendResponse(
                                exchange,
                                200,
                                "<html></html>"
                        );

                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        exchange.close();
                    } catch (IOException exception) {
                        exchange.close();
                    }
                }
        );

        JsoupHtmlClient shortTimeoutClient =
                new JsoupHtmlClient(
                        USER_AGENT,
                        100
                );

        IOException exception = assertThrows(
                IOException.class,
                () -> shortTimeoutClient.download(
                        resolve("/slow-response")
                )
        );

        assertTrue(
                exception instanceof SocketTimeoutException
                        || exception.getMessage()
                        .toLowerCase()
                        .contains("timed out")
        );
    }

    @Test
    void shouldRejectNullUri() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> htmlClient.download(null)
        );

        assertEquals(
                "URI must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectUriWithoutScheme() {
        URI uri = URI.create(
                "www.falabella.com/products"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> htmlClient.download(uri)
        );

        assertEquals(
                "URI scheme must be HTTP or HTTPS",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectFtpUri() {
        URI uri = URI.create(
                "ftp://www.falabella.com/products"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> htmlClient.download(uri)
        );

        assertEquals(
                "URI scheme must be HTTP or HTTPS",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectFileUri() {
        URI uri = URI.create(
                "file:///tmp/products.html"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> htmlClient.download(uri)
        );

        assertEquals(
                "URI scheme must be HTTP or HTTPS",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullUserAgent() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JsoupHtmlClient(
                        null,
                        TIMEOUT_MILLISECONDS
                )
        );

        assertEquals(
                "User agent must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectEmptyUserAgent() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JsoupHtmlClient(
                        "",
                        TIMEOUT_MILLISECONDS
                )
        );

        assertEquals(
                "User agent must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankUserAgent() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JsoupHtmlClient(
                        "   ",
                        TIMEOUT_MILLISECONDS
                )
        );

        assertEquals(
                "User agent must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectZeroTimeout() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JsoupHtmlClient(
                        USER_AGENT,
                        0
                )
        );

        assertEquals(
                "Timeout must be greater than zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNegativeTimeout() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new JsoupHtmlClient(
                        USER_AGENT,
                        -1
                )
        );

        assertEquals(
                "Timeout must be greater than zero",
                exception.getMessage()
        );
    }

    private URI resolve(String path) {
        return baseUri.resolve(path);
    }

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String body
    ) throws IOException {

        byte[] responseBytes = body.getBytes(
                StandardCharsets.UTF_8
        );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/html; charset=UTF-8"
        );

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        try (var responseBody = exchange.getResponseBody()) {
            responseBody.write(responseBytes);
        }
    }
}