package com.gft.wrk2025carrito.shopping_cart.domain.services;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.util.UUID;

public interface CartServices {

    /**
     *
     * If parameter id is null throws exception
     */
    void delete(UUID id);

    void update(Cart cart);
}
