package com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CartDetail {

    private Long productId;
    private int quantity;
    private BigDecimal totalPrice;
    private Double totalWeight;

    public static CartDetail build(Long productId, int quantity,  BigDecimal totalPrice, Double totalWeight) {

        if(productId == null){
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        if (totalPrice != null && totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (totalPrice != null && totalWeight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        return new CartDetail(productId, quantity ,totalPrice, totalWeight);
    }
}
