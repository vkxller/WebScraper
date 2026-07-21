package org.diegoreyes.webscraper.infrastructure.parser;

import org.diegoreyes.webscraper.domain.model.Product;
import org.diegoreyes.webscraper.port.ProductParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FalabellaProductParserTest {

    private ProductParser parser;

    @BeforeEach
    void setUp() {
        parser = new FalabellaProductParser();
    }

    @Test
    void shouldParseCompleteProduct() {
        String html = """
                <html>
                <body>
                    <div data-testid="ssr-pod">
                        <a href="https://www.falabella.com/falabella-cl/product/123">
                            <span class="pod-subTitle">
                                Notebook Lenovo IdeaPad
                            </span>

                            <span data-testid="final-price">
                                $499.990
                            </span>

                            <span data-testid="regular-price">
                                $599.990
                            </span>

                            <span class="discount-badge-item">
                                -17%
                            </span>
                        </a>
                    </div>
                </body>
                </html>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        Product product = products.getFirst();

        assertAll(
                () -> assertEquals(
                        "Falabella",
                        product.getStore()
                ),
                () -> assertEquals(
                        "Notebook Lenovo IdeaPad",
                        product.getName()
                ),
                () -> assertEquals(
                        new BigDecimal("499990"),
                        product.getPrice()
                ),
                () -> assertEquals(
                        new BigDecimal("599990"),
                        product.getPreviousPrice()
                ),
                () -> assertEquals(
                        "-17%",
                        product.getDiscount()
                ),
                () -> assertEquals(
                        "https://www.falabella.com/falabella-cl/product/123",
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldParseMultipleProducts() {
        String html = """
                <html>
                <body>
                    <div data-testid="ssr-pod">
                        <a href="https://www.falabella.com/product/111">
                            <span class="pod-subTitle">
                                Notebook HP
                            </span>

                            <span data-testid="final-price">
                                $399.990
                            </span>
                        </a>
                    </div>

                    <div data-testid="ssr-pod">
                        <a href="https://www.falabella.com/product/222">
                            <span class="pod-subTitle">
                                Notebook Asus
                            </span>

                            <span data-testid="final-price">
                                $749.990
                            </span>
                        </a>
                    </div>
                </body>
                </html>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(2, products.size());

        assertEquals(
                "Notebook HP",
                products.get(0).getName()
        );

        assertEquals(
                "Notebook Asus",
                products.get(1).getName()
        );
    }

    @Test
    void shouldParseProductWithoutPreviousPrice() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>

                        <span class="discount-badge-item">
                            -10%
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());
        assertNull(products.getFirst().getPreviousPrice());
        assertEquals("-10%", products.getFirst().getDiscount());
    }

    @Test
    void shouldParseProductWithoutDiscount() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>

                        <span data-testid="regular-price">
                            $599.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        Product product = products.getFirst();

        assertEquals(
                new BigDecimal("599990"),
                product.getPreviousPrice()
        );

        assertNull(product.getDiscount());
    }

    @Test
    void shouldParseProductWithoutPreviousPriceAndDiscount() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        Product product = products.getFirst();

        assertNull(product.getPreviousPrice());
        assertNull(product.getDiscount());
    }

    @Test
    void shouldRemoveCurrencySymbolsAndSeparatorsFromPrice() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            CLP $ 1.299.990
                        </span>

                        <span data-testid="regular-price">
                            Precio normal: $1.499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        Product product = products.getFirst();

        assertEquals(
                new BigDecimal("1299990"),
                product.getPrice()
        );

        assertEquals(
                new BigDecimal("1499990"),
                product.getPreviousPrice()
        );
    }

    @Test
    void shouldTrimProductText() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                                  Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                             $499.990
                        </span>

                        <span class="discount-badge-item">
                             -17%
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        Product product = products.getFirst();

        assertEquals(
                "Notebook Lenovo",
                product.getName()
        );

        assertEquals(
                "-17%",
                product.getDiscount()
        );
    }

    @Test
    void shouldIgnoreProductWithoutName() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span data-testid="final-price">
                            $499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldIgnoreProductWithBlankName() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldIgnoreProductWithoutCurrentPrice() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldIgnoreProductWithInvalidCurrentPrice() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            Price unavailable
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldIgnoreProductWithoutLink() {
        String html = """
                <div data-testid="ssr-pod">
                    <span class="pod-subTitle">
                        Notebook Lenovo
                    </span>

                    <span data-testid="final-price">
                        $499.990
                    </span>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldIgnoreProductWithBlankLink() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldKeepRelativeUrlWhenBaseUriIsNotAvailable() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="/falabella-cl/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        assertEquals(
                "/falabella-cl/product/123",
                products.getFirst().getSourceUrl()
        );
    }

    @Test
    void shouldIgnoreInvalidPreviousPriceWithoutIgnoringProduct() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>

                        <span data-testid="regular-price">
                            Not available
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());
        assertNull(products.getFirst().getPreviousPrice());
    }

    @Test
    void shouldReturnEmptyListWhenHtmlHasNoProducts() {
        String html = """
                <html>
                <body>
                    <h1>Falabella</h1>
                </body>
                </html>
                """;

        List<Product> products = parser.parse(html);

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenHtmlIsEmpty() {
        List<Product> products = parser.parse("");

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenHtmlIsBlank() {
        List<Product> products = parser.parse("   \n\t   ");

        assertTrue(products.isEmpty());
    }

    @Test
    void shouldRejectNullHtml() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> parser.parse(null)
        );

        assertEquals(
                "HTML must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldReturnImmutableList() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/123">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertThrows(
                UnsupportedOperationException.class,
                () -> products.clear()
        );
    }

    @Test
    void shouldIgnoreInvalidProductAndKeepValidProduct() {
        String html = """
                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/invalid">
                        <span class="pod-subTitle">
                            Product without price
                        </span>
                    </a>
                </div>

                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/valid">
                        <span class="pod-subTitle">
                            Notebook Lenovo
                        </span>

                        <span data-testid="final-price">
                            $499.990
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        assertEquals(
                "Notebook Lenovo",
                products.getFirst().getName()
        );
    }

    @Test
    void shouldOnlyParseElementsWithProductSelector() {
        String html = """
                <div class="unrelated-product">
                    <a href="https://www.falabella.com/product/111">
                        <span class="pod-subTitle">
                            Incorrect product
                        </span>

                        <span data-testid="final-price">
                            $100.000
                        </span>
                    </a>
                </div>

                <div data-testid="ssr-pod">
                    <a href="https://www.falabella.com/product/222">
                        <span class="pod-subTitle">
                            Correct product
                        </span>

                        <span data-testid="final-price">
                            $200.000
                        </span>
                    </a>
                </div>
                """;

        List<Product> products = parser.parse(html);

        assertEquals(1, products.size());

        assertEquals(
                "Correct product",
                products.getFirst().getName()
        );
    }
}