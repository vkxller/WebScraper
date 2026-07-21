package org.diegoreyes.webscraper.infrastructure.parser;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.port.ProductParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FalabellaProductParser implements ProductParser {

    private static final String STORE_NAME = "Falabella";

    private static final String PRODUCT_SELECTOR =
            "[data-testid=ssr-pod]";

    private static final String PRODUCT_NAME_SELECTOR =
            ".pod-subTitle";

    private static final String CURRENT_PRICE_SELECTOR =
            "[data-testid=final-price], "
                    + "[data-testid=price-0], "
                    + ".copy10.primary.medium";

    private static final String PREVIOUS_PRICE_SELECTOR =
            "[data-testid=regular-price], "
                    + "[data-testid=original-price], "
                    + ".copy3.secondary.regular.crossed";

    private static final String DISCOUNT_SELECTOR =
            ".discount-badge-item";

    private static final String PRODUCT_LINK_SELECTOR =
            "a[href]";

    @Override
    public List<Product> parse(String html) {
        Objects.requireNonNull(
                html,
                "HTML must not be null"
        );

        if (html.isBlank()) {
            return List.of();
        }

        Document document = Jsoup.parse(html);
        List<Product> products = new ArrayList<>();

        for (Element productElement
                : document.select(PRODUCT_SELECTOR)) {

            Product product = parseProduct(productElement);

            if (product != null) {
                products.add(product);
            }
        }

        return List.copyOf(products);
    }

    private Product parseProduct(Element productElement) {
        String name = extractText(
                productElement,
                PRODUCT_NAME_SELECTOR
        );

        BigDecimal price = extractPrice(
                productElement,
                CURRENT_PRICE_SELECTOR
        );

        String sourceUrl =
                extractSourceUrl(productElement);

        if (name == null
                || price == null
                || sourceUrl == null) {

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

        String text = selectedElement.text().trim();

        if (text.isBlank()) {
            return null;
        }

        return text;
    }

    private BigDecimal extractPrice(
            Element productElement,
            String selector
    ) {
        String priceText = extractText(
                productElement,
                selector
        );

        if (priceText == null) {
            return null;
        }

        String normalizedPrice =
                priceText.replaceAll("[^0-9]", "");

        if (normalizedPrice.isBlank()) {
            return null;
        }

        try {
            return new BigDecimal(normalizedPrice);

        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String extractSourceUrl(
            Element productElement
    ) {
        Element linkElement =
                productElement.selectFirst(
                        PRODUCT_LINK_SELECTOR
                );

        if (linkElement == null) {
            return null;
        }

        String sourceUrl =
                linkElement.attr("href").trim();

        if (sourceUrl.isBlank()) {
            return null;
        }

        return sourceUrl;
    }
}