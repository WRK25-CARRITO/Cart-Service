package com.gft.wrk2025carrito.shopping_cart.application.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CartCalculator {

    public static BigDecimal applyTax(BigDecimal price, Double taxRate)throws Exception  {
        validatePercentageAndAmount(price, taxRate, "Tax");
        BigDecimal normalizedTax = BigDecimal.valueOf(taxRate);

        BigDecimal calculatedPrice = price.multiply(BigDecimal.ONE.add(normalizedTax));
        return round(calculatedPrice);
    }

    public static BigDecimal applyCharge(BigDecimal price, Double charge)throws Exception  {
        validatePercentageAndAmount(price, charge, "Charge");
        BigDecimal normalizedCharge = BigDecimal.valueOf(charge);
        BigDecimal calculatedPrice = price.multiply(BigDecimal.ONE.add( normalizedCharge));
        return round(calculatedPrice);
    }

    private static void validatePercentageAndAmount(BigDecimal amount, Double rate, String context) throws Exception {
        if (rate == null) {
            throw new Exception(context + " rate cannot be null");
        }
        if (amount == null) {
            throw new Exception("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Amount cannot be negative");
        }
        if (rate < 0 || rate > 1) {
            throw new Exception(context + " rate must be between 0 and 1");
        }
    }

    private static BigDecimal round(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

}
