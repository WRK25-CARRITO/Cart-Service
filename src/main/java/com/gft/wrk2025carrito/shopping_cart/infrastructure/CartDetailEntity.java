package com.gft.wrk2025carrito.shopping_cart.infrastructure;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CartDetailEntity {

    private UUID cartId;
    private UUID productId;
    private int quantity;
    private double price;
    private double weight;

}
