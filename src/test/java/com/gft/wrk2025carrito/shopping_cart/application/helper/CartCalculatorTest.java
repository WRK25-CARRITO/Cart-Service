package com.gft.wrk2025carrito.shopping_cart.application.helper;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartCalculatorTest {


    @Test
    void InstanceCreation() {
        CartCalculator cartCalculator = new CartCalculator();
        assertNotNull(cartCalculator);
    }

    @Test
    void applyTax() throws Exception {
        BigDecimal price = new BigDecimal( "10.5");
        double tax = 0.21;
        BigDecimal expected = new BigDecimal("12.705");
        BigDecimal result = CartCalculator.applyTax(price, tax);
        assertEquals(expected, result);
    }

    @Test
    void applyTax_fail_price_null() {
        double tax = 0.21;
        assertThrows(Exception.class, () -> {
            CartCalculator.applyTax(null, tax);
        });
    }

    @Test
    void applyTax_fail_tax_null() {
        BigDecimal price = new BigDecimal( "10.5");
        assertThrows(Exception.class, () -> {
            CartCalculator.applyTax(price, null);
        });
    }
    @Test
    void applyTax_fail_tax_negative() {
        BigDecimal price = new BigDecimal( "10.5");
        double tax = -0.21;
        assertThrows(Exception.class, () -> {
            CartCalculator.applyTax(price, tax);
        });
    }

    @Test
    void applyTax_fail_tax_more_than_one() {
        BigDecimal price = new BigDecimal( "10.5");
        double tax = 1.21;
        assertThrows(Exception.class, () -> {
            CartCalculator.applyTax(price, tax);
        });
    }
    @Test
    void applyTax_fail_price_negative() {
        BigDecimal price = new BigDecimal( "-10.5");
        double tax = 0.21;
        assertThrows(Exception.class, () -> {
            CartCalculator.applyTax(price, tax);
        });
    }

}