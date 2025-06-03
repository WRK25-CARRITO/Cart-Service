package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionQuantity;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionSeason;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethodId;
import com.github.tomakehurst.wiremock.WireMockServer;
import jdk.jfr.Enabled;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartCalculatorTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void calculateAndUpdateCart_ShouldReturnValidCart() throws Exception {
        // 1. Mock del producto
        Product mockProduct = new Product(1L, "Product A", "TOYS", BigDecimal.valueOf(100), 50.0 );
        stubFor(post(urlEqualTo("/api/v1/products/list-by-ids"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(List.of(mockProduct)))));



        // 3. Crear CartDetail usando build
        CartDetail detail = CartDetail.build(
                1L, // productId
                2,  // quantity
                null, // totalPrice (lo calculará CartCalculator)
                null  // totalWeight
        );

        // 4. Crear Cart usando build
        Cart cart = Cart.build(
                new CartId(),
                UUID.randomUUID(),
                CountryTax.build(new CountryTaxId(),"ES", 0.21),
                PaymentMethod.build(new PaymentMethodId(),"Card", 0.05),
                null,
                null,
                new Date(),
                new Date(),
                List.of(detail),
                CartState.CLOSED,
                List.of(1L)
        );

        // 5. Ejecutar cálculo
        Cart result = CartCalculator.calculateAndUpdateCart(cart, restTemplate);

        // 6. Validaciones
        assertNotNull(result.getTotalPrice());
        System.out.println("TOTAL FINAL: " + result.getTotalPrice());

        // Precio esperado = 300.0 - 20% = 240 + 5% = 252 + 21% = 305.92
        BigDecimal expected = BigDecimal.valueOf(305.92).setScale(2);

        assertEquals(expected, result.getTotalPrice(), "El total final con promoción y tasas no es correcto");
        assertEquals(6.0, result.getTotalWeight(), "El peso total debe ser 3 * 2.0");
    }

    @Nested
    @DisplayName("applyTax tests")
    class ApplyTaxTests {

        @ParameterizedTest(name = "Price: {0}, Tax: {1} → Expected: {2}")
        @CsvSource({
                "100.00, 0.21, 121.00",
                "50.00, 0.00, 50.00"
        })
        void testApplyTax_ValidInputs(String priceStr, double taxRate, String expectedStr) throws Exception {
            BigDecimal price = new BigDecimal(priceStr);
            BigDecimal expected = new BigDecimal(expectedStr);

            BigDecimal result = CartCalculator.applyTax(price, taxRate);
            assertEquals(expected, result);
        }

        @Test
        void testApplyTax_NullRate_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(new BigDecimal("100"), null)
            );
            assertEquals("Tax rate cannot be null", ex.getMessage());
        }

        @Test
        void testApplyTax_NullPrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(null, 0.1)
            );
            assertEquals("Amount cannot be null", ex.getMessage());
        }

        @Test
        void testApplyTax_NegativePrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(new BigDecimal("-1"), 0.1)
            );
            assertEquals("Amount cannot be negative", ex.getMessage());
        }

        @ParameterizedTest
        @CsvSource({
                "1.1", "-0.1"
        })
        void testApplyTax_InvalidRate_shouldThrow(double invalidRate) {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(new BigDecimal("10"), invalidRate)
            );
            assertEquals("Tax rate must be between 0 and 1", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("applyCharge tests")
    class ApplyChargeTests {

        @ParameterizedTest(name = "Price: {0}, Charge: {1} → Expected: {2}")
        @CsvSource({
                "100.00, 0.10, 110.00",
                "50.00, 0.00, 50.00"
        })
        void testApplyCharge_ValidInputs(String priceStr, double chargeRate, String expectedStr) throws Exception {
            BigDecimal price = new BigDecimal(priceStr);
            BigDecimal expected = new BigDecimal(expectedStr);

            BigDecimal result = CartCalculator.applyCharge(price, chargeRate);
            assertEquals(expected, result);

        }

        @Test
        void testApplyCharge_NullRate_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(new BigDecimal("100"), null)
            );
            assertEquals("Charge rate cannot be null", ex.getMessage());
        }

        @Test
        void testApplyCharge_NullPrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(null, 0.1)
            );
            assertEquals("Amount cannot be null", ex.getMessage());
        }

        @Test
        void testApplyCharge_NegativePrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(new BigDecimal("-1"), 0.1)
            );
            assertEquals("Amount cannot be negative", ex.getMessage());
        }

        @ParameterizedTest
        @CsvSource({
                "1.5", "-0.1"
        })
        void testApplyCharge_InvalidRate_shouldThrow(double invalidRate) {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(new BigDecimal("10"), invalidRate)
            );
            assertEquals("Charge rate must be between 0 and 1", ex.getMessage());
        }
    }

}