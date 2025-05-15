package com.gft.wrk2025carrito.shopping_cart.domain.model;

import lombok.Getter;

@Getter
public class PaymentMethod {

    private PaymentMethodId id;
    private String paymentMethod;
    private double charge;
}
