package com.gft.wrk2025carrito.shopping_cart.domain.services;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.util.List;
import java.util.UUID;

public interface CartServices {

    /**
     *
     * If parameter id is null throws exception
     */
    void delete(UUID id);

    /**
     *
     * If parameter id is null throws exception
     */
    void deleteAllByUserId(UUID id);

    Cart createCart(UUID cart);

}
