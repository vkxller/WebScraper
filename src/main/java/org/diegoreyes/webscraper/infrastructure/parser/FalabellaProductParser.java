package org.diegoreyes.webscraper.infrastructure.parser;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.port.ProductParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FalabellaProductParser implements ProductParser {

    private static final String STORE_NAME =
            "Falabella";

    private static final String BASE_URL =
            "https://www.falabella.com";

    private static final String PRODUCT_SELECTOR =
            "a.pod-link, a[href*='/product/'], [data-pod=catalyst-pod], [data-testid=ssr-pod], .pod, div.pod-card, article.pod";

    private static final String PRODUCT_NAME_SELECTOR =
            "b.pod-subTitle, .pod-subTitle, [data-testid=pod-subTitle], [class*=pod-subTitle], [class*=pod-name]";

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
            ".discount-badge-item, span.discount";

    private static final Pattern PRICE_PATTERN =
            Pattern.compile(
                    "([0-9]{1,3}(?:\\.[0-9]{3})+|[0-9]+)"
            );

    private static final Pattern PRODUCT_ID_PATTERN =
            Pattern.compile("([0-9]{6,12})");

    private record ProductMetadata(String url, String imageUrl) {}

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
                Jsoup.parse(html, BASE_URL);

        List<ProductMetadata> nextDataList =
                parseNextDataList(document);

        Map<String, ProductMetadata> nextDataMap =
                parseNextDataMap(document);

        Elements productElements =
                document.select(PRODUCT_SELECTOR);

        List<Product> products =
                new ArrayList<>();

        Set<String> seenNames = new HashSet<>();

        for (int i = 0; i < productElements.size(); i++) {
            Element productElement = productElements.get(i);
            ProductMetadata positionalMetadata = i < nextDataList.size() ? nextDataList.get(i) : null;

            Product product =
                    parseProduct(productElement, positionalMetadata, nextDataMap);

            if (product != null) {
                // Deduplicate pods matched by multiple CSS selectors
                String key = product.getName() + "-" + product.getPrice();
                if (seenNames.add(key)) {
                    products.add(product);
                }
            }
        }

        return List.copyOf(products);
    }

    private Product parseProduct(
            Element productElement,
            ProductMetadata positionalMetadata,
            Map<String, ProductMetadata> nextDataMap
    ) {
        String name = extractText(
                productElement,
                PRODUCT_NAME_SELECTOR
        );

        BigDecimal price = extractPrice(
                productElement,
                CURRENT_PRICE_SELECTOR
        );

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

        String productId = extractProductId(productElement);

        ProductMetadata mapMetadata =
                productId != null ? nextDataMap.get(productId) : null;

        ProductMetadata metadata = positionalMetadata != null ? positionalMetadata : mapMetadata;

        String sourceUrl = extractLink(
                productElement,
                name,
                metadata
        );

        String imageUrl = extractImage(
                productElement,
                metadata
        );

        return new Product(
                STORE_NAME,
                name,
                price,
                previousPrice,
                discount,
                sourceUrl,
                imageUrl
        );
    }

    private String extractProductId(Element productElement) {
        Element keyElement = productElement.selectFirst("[data-key], [id*=testId-pod-], [id*=pod-]");
        if (keyElement != null) {
            String dataKey = keyElement.attr("data-key");
            if (!dataKey.isBlank() && dataKey.matches("[0-9]{6,12}")) {
                return dataKey;
            }
            String idAttr = keyElement.attr("id");
            Matcher m = PRODUCT_ID_PATTERN.matcher(idAttr);
            if (m.find()) {
                return m.group(1);
            }
        }

        String outerHtml = productElement.outerHtml();
        Matcher m = PRODUCT_ID_PATTERN.matcher(outerHtml);
        if (m.find()) {
            return m.group(1);
        }

        return null;
    }

    private String extractLink(
            Element productElement,
            String name,
            ProductMetadata metadata
    ) {
        // 1. Direct check on productElement itself if it is an <a href="..."> tag (e.g. a.pod-link)
        if (productElement.is("a[href]")) {
            String href = productElement.attr("href").trim();
            if (!href.isBlank() && !href.equals("#")) {
                String abs = productElement.absUrl("href");
                if (isValidProductUrl(abs)) {
                    return cleanUrl(abs);
                }
            }
        }

        // 2. Direct check on child <a href="..."> elements
        Elements anchors = productElement.select("a[href]");
        for (Element linkElement : anchors) {
            String href = linkElement.attr("href").trim();
            if (!href.isBlank() && !href.equals("#")) {
                String abs = linkElement.absUrl("href");
                if (isValidProductUrl(abs)) {
                    return cleanUrl(abs);
                }
            }
        }

        // 3. Check metadata from __NEXT_DATA__ JSON
        if (metadata != null && metadata.url() != null && !metadata.url().isBlank()) {
            return cleanUrl(metadata.url());
        }

        // 4. Construct canonical URL from extracted product ID and slug
        String productId = extractProductId(productElement);
        if (productId != null && name != null && !name.isBlank()) {
            String slug = slugifyPreserveCase(name);
            return BASE_URL + "/falabella-cl/product/" + productId + "/" + slug + "/" + productId;
        }

        return null;
    }

    private boolean isValidProductUrl(String url) {
        if (url == null || url.isBlank()) return false;
        if (!url.startsWith("http://") && !url.startsWith("https://")) return false;
        if (url.equalsIgnoreCase(BASE_URL) || url.equalsIgnoreCase(BASE_URL + "/")) return false;
        return true;
    }

    private String cleanUrl(String url) {
        if (url == null) return null;
        int queryIndex = url.indexOf("?");
        if (queryIndex != -1) {
            return url.substring(0, queryIndex);
        }
        return url;
    }

    private String slugifyPreserveCase(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-zA-Z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
        return normalized;
    }

    private String extractImage(Element productElement, ProductMetadata metadata) {
        Elements imageElements = productElement.select(
                "img[id^=testId-pod-image], img[data-testid=pod-image], .pod-image img, picture img, source, img"
        );

        for (Element el : imageElements) {
            String candidate = cleanAndValidateImageUrl(el.attr("data-src"));
            if (candidate != null) return candidate;

            candidate = cleanAndValidateImageUrl(el.attr("data-lazy-src"));
            if (candidate != null) return candidate;

            candidate = cleanAndValidateImageUrl(el.attr("data-original"));
            if (candidate != null) return candidate;

            candidate = cleanAndValidateImageUrl(firstFromSrcset(el.attr("srcset")));
            if (candidate != null) return candidate;

            candidate = cleanAndValidateImageUrl(firstFromSrcset(el.attr("data-srcset")));
            if (candidate != null) return candidate;

            candidate = cleanAndValidateImageUrl(el.attr("src"));
            if (candidate != null) return candidate;
        }

        String candidate = cleanAndValidateImageUrl(productElement.attr("data-pod-image"));
        if (candidate != null) return candidate;

        String candidateAttr = cleanAndValidateImageUrl(productElement.attr("data-image"));
        if (candidateAttr != null) return candidateAttr;

        if (metadata != null && metadata.imageUrl() != null && !metadata.imageUrl().isBlank()) {
            return cleanAndValidateImageUrl(metadata.imageUrl());
        }

        return null;
    }

    private List<ProductMetadata> parseNextDataList(Document document) {
        Element nextDataScript = document.selectFirst("script#__NEXT_DATA__");
        if (nextDataScript == null) {
            return List.of();
        }

        String json = nextDataScript.html();
        if (json.isBlank()) {
            return List.of();
        }

        List<String> urls = new ArrayList<>();
        Pattern urlPattern = Pattern.compile("\"url\":\"([^\"]*?/falabella-cl/product/[^\"]+)\"");
        Matcher urlMatcher = urlPattern.matcher(json);
        while (urlMatcher.find()) {
            String url = urlMatcher.group(1).replace("\\/", "/");
            if (!url.startsWith("http")) {
                url = BASE_URL + url;
            }
            urls.add(url);
        }

        List<String> imgs = new ArrayList<>();
        Pattern imgPattern = Pattern.compile("\"mediaUrls\":\\[\"([^\"]+)\"");
        Matcher imgMatcher = imgPattern.matcher(json);
        while (imgMatcher.find()) {
            String img = imgMatcher.group(1).replace("\\/", "/");
            if (!img.startsWith("http")) {
                img = "https:" + img;
            }
            imgs.add(img);
        }

        List<ProductMetadata> list = new ArrayList<>();
        int maxSize = Math.max(urls.size(), imgs.size());
        for (int i = 0; i < maxSize; i++) {
            String url = i < urls.size() ? urls.get(i) : null;
            String img = i < imgs.size() ? imgs.get(i) : null;
            list.add(new ProductMetadata(url, img));
        }

        return List.copyOf(list);
    }

    private Map<String, ProductMetadata> parseNextDataMap(Document document) {
        Element nextDataScript = document.selectFirst("script#__NEXT_DATA__");
        if (nextDataScript == null) {
            return Map.of();
        }

        String json = nextDataScript.html();
        if (json.isBlank()) {
            return Map.of();
        }

        Map<String, ProductMetadata> map = new HashMap<>();

        Map<String, String> imgMap = new HashMap<>();
        Pattern imgPattern = Pattern.compile("\"(?:skuId|productId)\":\"([^\"]+)\"[\\s\\S]*?\"mediaUrls\":\\[\"([^\"]+)\"");
        Matcher imgMatcher = imgPattern.matcher(json);
        while (imgMatcher.find()) {
            String id = imgMatcher.group(1);
            String img = imgMatcher.group(2).replace("\\/", "/");
            imgMap.putIfAbsent(id, img);
        }

        Map<String, String> urlMap = new HashMap<>();
        Pattern urlPattern = Pattern.compile("\"(?:skuId|productId)\":\"([^\"]+)\"[\\s\\S]*?\"url\":\"([^\"]+)\"");
        Matcher urlMatcher = urlPattern.matcher(json);
        while (urlMatcher.find()) {
            String id = urlMatcher.group(1);
            String url = urlMatcher.group(2).replace("\\/", "/");
            if (!url.startsWith("http")) {
                url = BASE_URL + url;
            }
            urlMap.putIfAbsent(id, url);
        }

        Set<String> allIds = new HashSet<>();
        allIds.addAll(imgMap.keySet());
        allIds.addAll(urlMap.keySet());

        for (String id : allIds) {
            map.put(id, new ProductMetadata(urlMap.get(id), imgMap.get(id)));
        }

        return Map.copyOf(map);
    }

    private String cleanAndValidateImageUrl(String rawCandidate) {
        if (rawCandidate == null || rawCandidate.isBlank()) {
            return null;
        }

        String trimmed = rawCandidate.trim();

        if (trimmed.startsWith("data:")
                || trimmed.contains("1x1")
                || trimmed.contains("blank.gif")
                || trimmed.contains("placeholder.gif")
                || trimmed.contains("transparent.png")) {
            return null;
        }

        if (trimmed.startsWith("//")) {
            trimmed = "https:" + trimmed;
        } else if (trimmed.startsWith("/")) {
            trimmed = BASE_URL + trimmed;
        }

        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            return null;
        }

        return trimmed;
    }

    private String firstFromSrcset(
            String srcset
    ) {
        if (srcset == null || srcset.isBlank()) {
            return "";
        }

        String firstCandidate =
                srcset.split(",")[0].trim();

        int spaceIndex =
                firstCandidate.indexOf(' ');

        return spaceIndex == -1
                ? firstCandidate
                : firstCandidate.substring(0, spaceIndex);
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