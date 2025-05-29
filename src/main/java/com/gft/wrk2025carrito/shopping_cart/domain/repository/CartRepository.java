package com.gft.wrk2025carrito.shopping_cart.domain.repository;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.util.List;
import java.util.UUID;

public interface CartRepository {

    void deleteById(UUID id);

    boolean existsById(UUID id);

    Cart save(CartEntity cartEntity);

    List<Cart> findByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);

    Cart findById(UUID id);


}
