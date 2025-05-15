package com.gft.wrk2025carrito.shopping_cart.domain.model;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Cart {

    private CartId id;
    private UUID userId;
    private double totalWeight;
    private CountryTaxId countryTaxId;
    private PaymentMethodId paymentMethodId;
    private double totalPrice;

}
