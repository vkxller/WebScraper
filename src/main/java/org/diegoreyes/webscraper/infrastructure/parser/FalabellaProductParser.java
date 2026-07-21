package org.diegoreyes.webscraper.infrastructure.parser;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.port.ProductParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FalabellaProductParser implements ProductParser {

    private static final String STORE_NAME =
            "Falabella";

    private static final String PRODUCT_SELECTOR =
            "[data-testid=ssr-pod]";

    private static final String PRODUCT_NAME_SELECTOR =
            ".pod-subTitle";

    private static final String CURRENT_PRICE_SELECTOR =
            "[data-testid=final-price], "
                    + "[data-testid=price-0], "
                    + "li.prices-0 span.copy10.primary.medium, "
                    + ".copy10.primary.medium";

    private static final String PREVIOUS_PRICE_SELECTOR =
            "[data-testid=regular-price], "
                    + "[data-testid=original-price], "
                    + "li[data-normal-price] span, "
                    + "span.crossed";

    private static final String DISCOUNT_SELECTOR =
            ".discount-badge-item";

    private static final Pattern PRICE_PATTERN =
            Pattern.compile(
                    "([0-9]{1,3}(?:\\.[0-9]{3})+|[0-9]+)"
            );

    @Override
    public List<Product> parse(String html) {
        Objects.requireNonNull(
                html,
                "HTML must not be null"
        );

        if (html.isBlank()) {
            return List.of();
        }

        Document document =
                Jsoup.parse(html);

        Elements productElements =
                document.select(PRODUCT_SELECTOR);

        List<Product> products =
                new ArrayList<>();

        for (Element productElement : productElements) {
            Product product =
                    parseProduct(productElement);

            if (product != null) {
                products.add(product);
            }
        }

        return List.copyOf(products);
    }

    private Product parseProduct(
            Element productElement
    ) {
        String name = extractText(
                productElement,
                PRODUCT_NAME_SELECTOR
        );

        BigDecimal price = extractPrice(
                productElement,
                CURRENT_PRICE_SELECTOR
        );

        /*
         * Name and current price are the only required
         * fields for creating a product.
         */
        if (name == null || price == null) {
            return null;
        }

        BigDecimal previousPrice = extractPrice(
                productElement,
                PREVIOUS_PRICE_SELECTOR
        );

        String discount = extractText(
                productElement,
                DISCOUNT_SELECTOR
        );

        /*
         * Falabella currently sends an empty href in the
         * initial HTML downloaded by Jsoup. The product URL
         * will be implemented later using a solution capable
         * of processing dynamic JavaScript content.
         */
        String sourceUrl = null;

        return new Product(
                STORE_NAME,
                name,
                price,
                previousPrice,
                discount,
                sourceUrl
        );
    }

    private String extractText(
            Element productElement,
            String selector
    ) {
        Element selectedElement =
                productElement.selectFirst(selector);

        if (selectedElement == null) {
            return null;
        }

        String text =
                selectedElement.text().trim();

        return text.isBlank()
                ? null
                : text;
    }

    private BigDecimal extractPrice(
            Element productElement,
            String selector
    ) {
        Elements priceElements =
                productElement.select(selector);

        for (Element priceElement : priceElements) {
            BigDecimal price =
                    parseFirstPrice(
                            priceElement.text()
                    );

            if (price != null) {
                return price;
            }
        }

        return null;
    }

    private BigDecimal parseFirstPrice(
            String priceText
    ) {
        if (priceText == null
                || priceText.isBlank()) {

            return null;
        }

        Matcher matcher =
                PRICE_PATTERN.matcher(priceText);

        if (!matcher.find()) {
            return null;
        }

        String normalizedPrice =
                matcher.group(1)
                        .replace(".", "");

        try {
            return new BigDecimal(
                    normalizedPrice
            );

        } catch (NumberFormatException exception) {
            return null;
        }
    }
}