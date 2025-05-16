package com.gft.wrk2025carrito.shopping_cart.domain.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {

    private CartId id;
    private UUID userId;
    private double totalWeight;
    private CountryTaxId countryTaxId;
    private PaymentMethodId paymentMethodId;
    private double totalPrice;
    private List<CartDetail> cartDetails;

    static Cart build(CartId id,UUID userId, CountryTaxId countryTaxId, PaymentMethodId paymentMethodId, double totalPrice, double totalWeight, List<CartDetail> cartDetails) {

        if (totalPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (totalWeight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if(cartDetails == null) {
            throw new IllegalArgumentException("Cart details cannot be null");
        }

        return new Cart(id, userId, totalWeight, countryTaxId, paymentMethodId, totalPrice, cartDetails);
    }

}
