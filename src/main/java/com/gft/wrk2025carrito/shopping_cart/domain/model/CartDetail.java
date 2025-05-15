package com.gft.wrk2025carrito.shopping_cart.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CartDetail {

    private CartId cartId;
    private UUID productId;
    private int quantity;
    private double price;
    private double weight;

}
