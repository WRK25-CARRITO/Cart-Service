package com.gft.wrk2025carrito.shopping_cart.application.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartCalculatorTest {

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