package org.diegoreyes.webscraper.infrastructure.client;

import org.diegoreyes.webscraper.port.HtmlClient;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public final class JsoupHtmlClient implements HtmlClient {

    private static final String DEFAULT_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/131.0.0.0 Safari/537.36";

    private static final int DEFAULT_TIMEOUT_MILLIS = 10_000;

    private final String userAgent;
    private final int timeoutMillis;

    public JsoupHtmlClient() {
        this(
                DEFAULT_USER_AGENT,
                DEFAULT_TIMEOUT_MILLIS
        );
    }

    public JsoupHtmlClient(
            String userAgent,
            int timeoutMillis
    ) {
        if (userAgent == null || userAgent.isBlank()) {
            throw new IllegalArgumentException(
                    "User agent must not be blank"
            );
        }

        if (timeoutMillis <= 0) {
            throw new IllegalArgumentException(
                    "Timeout must be greater than zero"
            );
        }

        this.userAgent = userAgent.trim();
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public String download(URI uri) throws IOException {
        Objects.requireNonNull(
                uri,
                "URI must not be null"
        );

        validateScheme(uri);

        return Jsoup.connect(uri.toString())
                .userAgent(userAgent)
                .timeout(timeoutMillis)
                .followRedirects(true)
                .ignoreHttpErrors(false)
                .execute()
                .body();
    }

    private void validateScheme(URI uri) {
        String scheme = uri.getScheme();

        boolean isHttp =
                "http".equalsIgnoreCase(scheme);

        boolean isHttps =
                "https".equalsIgnoreCase(scheme);

        if (!isHttp && !isHttps) {
            throw new IllegalArgumentException(
                    "URI scheme must be HTTP or HTTPS"
            );
        }
    }
}