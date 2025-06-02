package com.gft.wrk2025carrito.shopping_cart.application.helper;

import java.math.BigDecimal;

public class CartCalculator {

    public static BigDecimal applyTax(BigDecimal price, Double taxRate)throws Exception  {
        if (taxRate == null) {
            throw new Exception("Tax rate cannot be null");
        }
        if (price == null) {
            throw new Exception("Price cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Price cannot be negative");
        }

        if ( taxRate > 1 || taxRate < 0) {
            throw new Exception("Tax rate must be between 0 and 1");
        }
        BigDecimal normalizedTax = BigDecimal.valueOf(taxRate);
        return price.multiply(BigDecimal.ONE.add(normalizedTax));
    }
}
