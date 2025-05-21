package com.gft.wrk2025carrito.shopping_cart.domain.model;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartDetailTest {

    @Test
    void create_CartDetail_ok() {
        UUID productId = UUID.randomUUID();
        int quantity = 3;
        double price = 1.5;
        double weight = 20.5;

        CartDetail cartDetail = CartDetail.build(productId,quantity, price, weight);

        assertEquals(3,cartDetail.getQuantity());
        assertEquals(1.5,cartDetail.getPrice());
        assertEquals(20.5,cartDetail.getWeight());
    }

    @Test
    void create_CartDetail_negative_quantity() {
        UUID productId = UUID.randomUUID();
        int quantity = -3;
        double price = 1.5;
        double weight = 20.5;

        assertThrows(IllegalArgumentException.class, () -> {
            CartDetail.build(productId,quantity, price, weight);
        });
    }

    @Test
    void create_CartDetail_negative_price() {
        UUID productId = UUID.randomUUID();
        int quantity = 3;
        double price = -1.5;
        double weight = 20.5;

        assertThrows(IllegalArgumentException.class, () -> {
            CartDetail.build(productId,quantity, price, weight);
        });
    }

    @Test
    void create_CartDetail_negative_weight() {
        UUID productId = UUID.randomUUID();
        int quantity = 3;
        double price = 1.5;
        double weight = -20.5;

        assertThrows(IllegalArgumentException.class, () -> {
            CartDetail.build(productId,quantity, price, weight);
        });
    }
}