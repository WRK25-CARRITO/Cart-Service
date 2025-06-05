package com.gft.wrk2025carrito.shopping_cart.application.dto;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;

public record CartDTO(
        CartState cartState,
        CountryTax countryTax,
        PaymentMethod paymentMethod
) {
}
