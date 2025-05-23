package com.gft.wrk2025carrito.shopping_cart.domain.model;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartDetailTest {

    @Test
    void create_CartDetail_ok() {
        UUID productId = UUID.randomUUID();
        CartId cartId = new CartId();
        int quantity = 3;
        double price = 1.5;
        double weight = 20.5;

        CartDetail cartDetail = CartDetail.build(productId,quantity, BigDecimal.valueOf(price), weight);

        assertEquals(3,cartDetail.getQuantity());
        assertEquals(BigDecimal.valueOf(1.5),cartDetail.getTotalPrice());
        assertEquals(20.5,cartDetail.getTotalWeight());
    }

    @Test
    void create_CartDetail_negative_quantity() {
        UUID productId = UUID.randomUUID();
        CartId cartId = new CartId();
        int quantity = -3;
        double price = 1.5;
        double weight = 20.5;

        assertThrows(IllegalArgumentException.class, () -> {
            CartDetail.build(productId,quantity, BigDecimal.valueOf(price), weight);
        });
    }

    @Test
    void create_CartDetail_fail_productId_null() {
        CartId cartId = new CartId();
        int quantity = 3;
        double price = 1.5;
        double weight = 20.5;

        assertThrows(IllegalArgumentException.class, () -> {
            CartDetail.build(null,quantity, BigDecimal.valueOf(price), weight);
        });
    }

    @Test
    void create_CartDetail_negative_price() {
        UUID productId = UUID.randomUUID();
        CartId cartId = new CartId();
        int quantity = 3;
        double price = -1.5;
        double weight = 20.5;

        assertThrows(IllegalArgumentException.class, () -> {
            CartDetail.build(productId,quantity, BigDecimal.valueOf(price), weight);
        });
    }

    @Test
    void create_CartDetail_negative_weight() {
        UUID productId = UUID.randomUUID();
        CartId cartId = new CartId();
        int quantity = 3;
        double price = 1.5;
        double weight = -20.5;

        assertThrows(IllegalArgumentException.class, () -> {
            CartDetail.build(productId,quantity, BigDecimal.valueOf(price), weight);
        });
    }
}