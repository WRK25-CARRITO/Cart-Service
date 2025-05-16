package com.gft.wrk2025carrito.shopping_cart.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartDetail {

    private CartId cartId;
    private UUID productId;
    private int quantity;
    private double price;
    private double weight;

    static CartDetail build(CartId cartId, UUID productId, int quantity, double price, double weight) {

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        return new CartDetail(cartId, productId, quantity, price, weight);
    }
}
