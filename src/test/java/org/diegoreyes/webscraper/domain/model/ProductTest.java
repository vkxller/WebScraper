package org.diegoreyes.webscraper.domain.model;

import org.diegoreyes.webscraper.domain.exception.InvalidProductException;
import org.diegoreyes.webscraper.domain.valueobject.Discount;
import org.diegoreyes.webscraper.domain.valueobject.Price;
import org.diegoreyes.webscraper.domain.valueobject.ProductId;
import org.diegoreyes.webscraper.domain.valueobject.ProductName;
import org.diegoreyes.webscraper.domain.valueobject.ProductUrl;
import org.diegoreyes.webscraper.domain.valueobject.StoreName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private static final ProductId ID =
            ProductId.of("test-prod-123");

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

    private static final String IMAGE_URL =
            "https://www.falabella.com/image/123.jpg";

    @Test
    void shouldCreateProductWithAllFieldsUsingValueObjects() {
        Product product = new Product(
                ID,
                new StoreName(STORE),
                new ProductName(NAME),
                new Price(PRICE),
                new Price(PREVIOUS_PRICE),
                new Discount(DISCOUNT),
                new ProductUrl(SOURCE_URL),
                new ProductUrl(IMAGE_URL)
        );

        assertAll(
                () -> assertEquals(ID, product.getId()),
                () -> assertEquals(STORE, product.getStore()),
                () -> assertEquals(STORE, product.getStoreName().value()),
                () -> assertEquals(NAME, product.getName()),
                () -> assertEquals(NAME, product.getProductName().value()),
                () -> assertEquals(PRICE, product.getPrice()),
                () -> assertEquals(PRICE, product.getProductPrice().amount()),
                () -> assertEquals(PREVIOUS_PRICE, product.getPreviousPrice()),
                () -> assertEquals(PREVIOUS_PRICE, product.getProductPreviousPrice().amount()),
                () -> assertEquals(DISCOUNT, product.getDiscount()),
                () -> assertEquals(DISCOUNT, product.getProductDiscount().value()),
                () -> assertEquals(SOURCE_URL, product.getSourceUrl()),
                () -> assertEquals(SOURCE_URL, product.getProductSourceUrl().value()),
                () -> assertEquals(IMAGE_URL, product.getImageUrl()),
                () -> assertEquals(IMAGE_URL, product.getProductImageUrl().value())
        );
    }

    @Test
    void shouldCreateProductWithAllFields() {
        Product product = new Product(
                ID,
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL,
                IMAGE_URL
        );

        assertAll(
                () -> assertEquals(ID, product.getId()),
                () -> assertEquals(STORE, product.getStore()),
                () -> assertEquals(NAME, product.getName()),
                () -> assertEquals(PRICE, product.getPrice()),
                () -> assertEquals(PREVIOUS_PRICE, product.getPreviousPrice()),
                () -> assertEquals(DISCOUNT, product.getDiscount()),
                () -> assertEquals(SOURCE_URL, product.getSourceUrl()),
                () -> assertEquals(IMAGE_URL, product.getImageUrl())
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
                () -> assertNotNull(product.getId()),
                () -> assertNull(product.getPreviousPrice()),
                () -> assertNull(product.getProductPreviousPrice()),
                () -> assertNull(product.getDiscount()),
                () -> assertNull(product.getProductDiscount()),
                () -> assertNull(product.getSourceUrl()),
                () -> assertNull(product.getProductSourceUrl()),
                () -> assertNull(product.getImageUrl()),
                () -> assertNull(product.getProductImageUrl())
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
                "   ",
                "   "
        );

        assertAll(
                () -> assertNull(product.getDiscount()),
                () -> assertNull(product.getSourceUrl()),
                () -> assertNull(product.getImageUrl())
        );
    }

    @Test
    void shouldTrimTextFields() {
        Product product = new Product(
                ID,
                "  Falabella  ",
                "  Notebook Lenovo  ",
                PRICE,
                PREVIOUS_PRICE,
                "  -17%  ",
                "  https://www.falabella.com/product/123  ",
                "  https://www.falabella.com/image/123.jpg  "
        );

        assertAll(
                () -> assertEquals(STORE, product.getStore()),
                () -> assertEquals(NAME, product.getName()),
                () -> assertEquals(DISCOUNT, product.getDiscount()),
                () -> assertEquals(SOURCE_URL, product.getSourceUrl()),
                () -> assertEquals(IMAGE_URL, product.getImageUrl())
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

        assertEquals(BigDecimal.ZERO, product.getPrice());
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

        assertEquals(BigDecimal.ZERO, product.getPreviousPrice());
    }

    @Test
    void shouldRejectNullStore() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        ID,
                        null,
                        NAME,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL,
                        IMAGE_URL
                )
        );

        assertEquals("Store must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankStore() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        ID,
                        "   ",
                        NAME,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL,
                        IMAGE_URL
                )
        );

        assertEquals("Store must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectNullName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        ID,
                        STORE,
                        null,
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL,
                        IMAGE_URL
                )
        );

        assertEquals("Product name must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankName() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        ID,
                        STORE,
                        "   ",
                        PRICE,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL,
                        IMAGE_URL
                )
        );

        assertEquals("Product name must not be blank", exception.getMessage());
    }

    @Test
    void shouldRejectNullPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        ID,
                        STORE,
                        NAME,
                        null,
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL,
                        IMAGE_URL
                )
        );

        assertEquals("Price must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void shouldRejectNegativePrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        ID,
                        STORE,
                        NAME,
                        new BigDecimal("-1"),
                        PREVIOUS_PRICE,
                        DISCOUNT,
                        SOURCE_URL,
                        IMAGE_URL
                )
        );

        assertEquals("Price must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void shouldRejectNegativePreviousPrice() {
        InvalidProductException exception = assertThrows(
                InvalidProductException.class,
                () -> new Product(
                        ID,
                        STORE,
                        NAME,
                        PRICE,
                        new BigDecimal("-1"),
                        DISCOUNT,
                        SOURCE_URL,
                        IMAGE_URL
                )
        );

        assertEquals("Previous price must be greater than or equal to zero", exception.getMessage());
    }

    @Test
    void shouldBeEqualWhenIdentityIsSame() {
        Product firstProduct = createProduct(ID);
        Product secondProduct = createProduct(ID);

        assertAll(
                () -> assertEquals(firstProduct, secondProduct),
                () -> assertEquals(firstProduct.hashCode(), secondProduct.hashCode())
        );
    }

    @Test
    void shouldNotBeEqualWhenIdentityIsDifferent() {
        Product firstProduct = createProduct(ProductId.of("id-1"));
        Product secondProduct = createProduct(ProductId.of("id-2"));

        assertNotEquals(firstProduct, secondProduct);
    }

    @Test
    void shouldBeEqualToItself() {
        Product product = createProduct(ID);

        assertEquals(product, product);
    }

    @Test
    void shouldNotBeEqualToNull() {
        Product product = createProduct(ID);

        assertNotEquals(product, null);
    }

    @Test
    void shouldNotBeEqualToAnotherType() {
        Product product = createProduct(ID);

        assertNotEquals(product, "Product");
    }

    @Test
    void shouldContainProductDataInToString() {
        String result = createProduct(ID).toString();

        assertAll(
                () -> assertTrue(result.contains(STORE)),
                () -> assertTrue(result.contains(NAME)),
                () -> assertTrue(result.contains(PRICE.toString())),
                () -> assertTrue(result.contains(PREVIOUS_PRICE.toString())),
                () -> assertTrue(result.contains(DISCOUNT)),
                () -> assertTrue(result.contains(SOURCE_URL))
        );
    }

    @Test
    void shouldRepresentNullOptionalFieldsInToString() {
        Product product = new Product(
                ID,
                STORE,
                NAME,
                PRICE,
                null,
                null,
                null,
                null
        );

        String result = product.toString();

        assertAll(
                () -> assertTrue(result.contains("previousPrice=null")),
                () -> assertTrue(result.contains("discount='null'")),
                () -> assertTrue(result.contains("sourceUrl='null'")),
                () -> assertTrue(result.contains("imageUrl='null'"))
        );
    }

    private Product createProduct(ProductId id) {
        return new Product(
                id,
                STORE,
                NAME,
                PRICE,
                PREVIOUS_PRICE,
                DISCOUNT,
                SOURCE_URL,
                IMAGE_URL
        );
    }
}