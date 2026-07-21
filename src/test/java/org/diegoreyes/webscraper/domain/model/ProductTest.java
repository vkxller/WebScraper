package org.diegoreyes.webscraper.domain.model;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private static final String STORE = "Falabella";
    private static final String NAME = "Notebook Lenovo";
    private static final BigDecimal PRICE =
            new BigDecimal("499990");
    private static final BigDecimal PREVIOUS_PRICE =
            new BigDecimal("599990");
    private static final String DISCOUNT = "-17%";
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
    void shouldCreateProductWithoutPreviousPrice() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                null,
                DISCOUNT,
                SOURCE_URL
        );

        assertNull(product.getPreviousPrice());
    }

    @Test
    void shouldCreateProductWithoutDiscount() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                null,
                SOURCE_URL
        );

        assertNull(product.getDiscount());
    }

    @Test
    void shouldNormalizeBlankDiscountToNull() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                "   ",
                SOURCE_URL
        );

        assertNull(product.getDiscount());
    }

    @Test
    void shouldTrimRequiredTextFields() {
        Product product = new Product(
                "  Falabella  ",
                "  Notebook Lenovo  ",
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
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
                        SOURCE_URL,
                        product.getSourceUrl()
                )
        );
    }

    @Test
    void shouldTrimDiscount() {
        Product product = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                "  -17%  ",
                SOURCE_URL
        );

        assertEquals(
                DISCOUNT,
                product.getDiscount()
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
        InvalidProductException exception = assertThrows(
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
        InvalidProductException exception = assertThrows(
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
        InvalidProductException exception = assertThrows(
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
        InvalidProductException exception = assertThrows(
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
        InvalidProductException exception = assertThrows(
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
        InvalidProductException exception = assertThrows(
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
        InvalidProductException exception = assertThrows(
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
    void shouldRejectNullSourceUrl() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        STORE,
                        NAME,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        null
                )
        );

        assertEquals(
                "Source URL must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankSourceUrl() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        STORE,
                        NAME,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        "   "
                )
        );

        assertEquals(
                "Source URL must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        Product firstProduct = createProduct();
        Product secondProduct = createProduct();

        assertEquals(
                firstProduct,
                secondProduct
        );

        assertEquals(
                firstProduct.hashCode(),
                secondProduct.hashCode()
        );
    }

    @Test
    void shouldBeEqualToItself() {
        Product product = createProduct();

        assertEquals(
                product,
                product
        );
    }

    @Test
    void shouldNotBeEqualToNull() {
        Product product = createProduct();

        assertNotEquals(
                product,
                null
        );
    }

    @Test
    void shouldNotBeEqualToDifferentObjectType() {
        Product product = createProduct();

        assertNotEquals(
                product,
                "Product"
        );
    }

    @Test
    void shouldNotBeEqualWhenStoreIsDifferent() {
        Product differentProduct = new Product(
                "Ripley",
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenNameIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                "Notebook HP",
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenPriceIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                new BigDecimal("399990"),
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenPreviousPriceIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                PRICE,
                new BigDecimal("649990"),
                DISCOUNT,
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenDiscountIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                "-10%",
                SOURCE_URL
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldNotBeEqualWhenSourceUrlIsDifferent() {
        Product differentProduct = new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                "https://www.falabella.com/product/999"
        );

        assertNotEquals(
                createProduct(),
                differentProduct
        );
    }

    @Test
    void shouldReturnReadableToString() {
        Product product = createProduct();

        String result = product.toString();

        assertAll(
                () -> assertTrue(result.contains("Falabella")),
                () -> assertTrue(result.contains("Notebook Lenovo")),
                () -> assertTrue(result.contains("499990")),
                () -> assertTrue(result.contains("599990")),
                () -> assertTrue(result.contains("-17%")),
                () -> assertTrue(result.contains(SOURCE_URL))
        );
    }

    private Product createProduct() {
        return new Product(
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL
        );
    }
}