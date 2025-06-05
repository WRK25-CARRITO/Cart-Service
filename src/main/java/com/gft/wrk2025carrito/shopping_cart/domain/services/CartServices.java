package com.gft.wrk2025carrito.shopping_cart.domain.services;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
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

    void update(UUID cartId, Map<Long, Integer> cartProducts);

    Cart createCart(UUID cart);

    List<Cart> getAll();

    Cart getById(UUID id);

    Cart updateState(UUID cartId, CartDTO cartDTO);

    @Transactional
    List<Cart> getAllActiveCarts();

    Cart showTotalPriceAndWeight(UUID id) throws Exception;
}
