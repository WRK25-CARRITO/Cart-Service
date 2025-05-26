package com.gft.wrk2025carrito.shopping_cart.domain.services;

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

}
