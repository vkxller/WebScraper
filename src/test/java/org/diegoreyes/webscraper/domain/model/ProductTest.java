package org.diegoreyes.webscraper.domain.model;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private static final String STORE =
            "Falabella";

    private static final String NAME =
            "Notebook Lenovo";

    private static final BigDecimal PRICE =
            new BigDecimal("499990");

    private static final BigDecimal PREVIOUS_PRICE =
            new BigDecimal("599990");

    private static final String DISCOUNT =
            "-17%";

    private static final String SOURCE_URL =
            "https://www.falabella.com/product/123";

    @Test
    void shouldCreateProductWithAllFields() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertAll(
                () -> assertEquals(
                        STORE,
                        product.getStore()
                ),
                () -> assertEquals(
                        NAME,
                        product.getName()
                ),
                () -> assertEquals(
                        PRICE,
                        product.getPrice()
                ),
                () -> assertEquals(
                        PREVIOUS_PRICE,
                        product.getPreviousPrice()
                ),
                () -> assertEquals(
                        DISCOUNT,
                        product.getDiscount()
                ),
                () -> assertEquals(
                        SOURCE_URL,
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldCreateProductWithoutOptionalFields() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                null,
                null
        );

        assertAll(
                () -> assertNull(
                        product.getPreviousPrice()
                ),
                () -> assertNull(
                        product.getDiscount()
                ),
                () -> assertNull(
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldNormalizeBlankOptionalFieldsToNull() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                "   ",
                "   "
        );

        assertAll(
                () -> assertNull(
                        product.getDiscount()
                ),
                () -> assertNull(
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldTrimTextFields() {
        Product product = new Product(
                "  Falabella  ",
                "  Notebook Lenovo  ",
                PRICE,
                PREVIOUS_PRICE,
                "  -17%  ",
                "  https://www.falabella.com/product/123  "
        );

        assertAll(
                () -> assertEquals(
                        STORE,
                        product.getStore()
                ),
                () -> assertEquals(
                        NAME,
                        product.getName()
                ),
                () -> assertEquals(
                        DISCOUNT,
                        product.getDiscount()
                ),
                () -> assertEquals(
                        SOURCE_URL,
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldAcceptZeroPrice() {
        Product product = new Product(
                STORE,
                NAME,
                BigDecimal.ZERO,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertEquals(
                BigDecimal.ZERO,
                product.getPrice()
        );
    }

    @Test
    void shouldAcceptZeroPreviousPrice() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                BigDecimal.ZERO,
                DISCOUNT,
                SOURCE_URL
        );

        assertEquals(
                BigDecimal.ZERO,
                product.getPreviousPrice()
        );
    }

    @Test
    void shouldRejectNullStore() {
        InvalidProductException exception =
                assertThrows(
                        InvalidProductException.class,
                        () -> new Product(
                                null,
                                NAME,
                                PRICE,
                                PREVIOUS_PRICE,
                                DISCOUNT,
                                SOURCE_URL
                        )
                );

        assertEquals(
                "Store must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankStore() {
        InvalidProductException exception =
                assertThrows(
                        InvalidProductException.class,
                        () -> new Product(
                                "   ",
                                NAME,
                                PRICE,
                                PREVIOUS_PRICE,
                                DISCOUNT,
                                SOURCE_URL
                        )
                );

        assertEquals(
                "Store must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullName() {
        InvalidProductException exception =
                assertThrows(
                        InvalidProductException.class,
                        () -> new Product(
                                STORE,
                                null,
                                PRICE,
                                PREVIOUS_PRICE,
                                DISCOUNT,
                                SOURCE_URL
                        )
                );

        assertEquals(
                "Product name must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankName() {
        InvalidProductException exception =
                assertThrows(
                        InvalidProductException.class,
                        () -> new Product(
                                STORE,
                                "   ",
                                PRICE,
                                PREVIOUS_PRICE,
                                DISCOUNT,
                                SOURCE_URL
                        )
                );

        assertEquals(
                "Product name must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullPrice() {
        InvalidProductException exception =
                assertThrows(
                        InvalidProductException.class,
                        () -> new Product(
                                STORE,
                                NAME,
                                null,
                                PREVIOUS_PRICE,
                                DISCOUNT,
                                SOURCE_URL
                        )
                );

        assertEquals(
                "Price must be greater than or equal to zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNegativePrice() {
        InvalidProductException exception =
                assertThrows(
                        InvalidProductException.class,
                        () -> new Product(
                                STORE,
                                NAME,
                                new BigDecimal("-1"),
                                PREVIOUS_PRICE,
                                DISCOUNT,
                                SOURCE_URL
                        )
                );

        assertEquals(
                "Price must be greater than or equal to zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNegativePreviousPrice() {
        InvalidProductException exception =
                assertThrows(
                        InvalidProductException.class,
                        () -> new Product(
                                STORE,
                                NAME,
                                PRICE,
                                new BigDecimal("-1"),
                                DISCOUNT,
                                SOURCE_URL
                        )
                );

        assertEquals(
                "Previous price must be greater than or equal to zero",
                exception.getMessage()
        );
    }

    @Test
    void shouldBeEqualWhenFieldsAreEqual() {
        Product firstProduct =
                createProduct(SOURCE_URL);

        Product secondProduct =
                createProduct(SOURCE_URL);

        assertAll(
                () -> assertEquals(
                        firstProduct,
                        secondProduct
                ),
                () -> assertEquals(
                        firstProduct.hashCode(),
                        secondProduct.hashCode()
                )
        );
    }

    @Test
    void shouldBeEqualWhenBothUrlsAreNull() {
        Product firstProduct =
                createProduct(null);

        Product secondProduct =
                createProduct(null);

        assertAll(
                () -> assertEquals(
                        firstProduct,
                        secondProduct
                ),
                () -> assertEquals(
                        firstProduct.hashCode(),
                        secondProduct.hashCode()
                )
        );
    }

    @Test
    void shouldNotBeEqualWhenSourceUrlIsDifferent() {
        Product firstProduct =
                createProduct(null);

        Product secondProduct =
                createProduct(SOURCE_URL);

        assertNotEquals(
                firstProduct,
                secondProduct
        );
    }

    @Test
    void shouldNotBeEqualToNull() {
        assertNotEquals(
                createProduct(null),
                null
        );
    }

    @Test
    void shouldNotBeEqualToAnotherType() {
        assertNotEquals(
                createProduct(null),
                "Product"
        );
    }

    @Test
    void shouldContainProductDataInToString() {
        String result =
                createProduct(null).toString();

        assertAll(
                () -> assertTrue(
                        result.contains(STORE)
                ),
                () -> assertTrue(
                        result.contains(NAME)
                ),
                () -> assertTrue(
                        result.contains(
                                PRICE.toString()
                        )
                ),
                () -> assertTrue(
                        result.contains(
                                "sourceUrl='null'"
                        )
                )
        );
    }

    private Product createProduct(
            String sourceUrl
    ) {
        return new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                sourceUrl
        );
    }
}