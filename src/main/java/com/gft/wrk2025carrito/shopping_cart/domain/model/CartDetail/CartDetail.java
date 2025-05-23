package com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartDetail {
    private UUID productId;
    private int quantity;
    private BigDecimal totalPrice;
    private double totalWeight;

    public static CartDetail build(UUID productId, int quantity, BigDecimal price, double weight) {

        if (price.compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (weight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        return new CartDetail(productId, quantity, price, weight);
    }
}
