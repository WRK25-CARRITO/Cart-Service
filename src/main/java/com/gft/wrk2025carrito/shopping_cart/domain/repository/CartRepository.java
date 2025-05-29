package com.gft.wrk2025carrito.shopping_cart.domain.repository;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.util.List;
import java.util.UUID;

public interface CartRepository {

    void deleteById(UUID id);

    boolean existsById(UUID id);

    List<Cart> findByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);

    boolean cartExistsByUserIdAndStateActive(UUID userId);

    Cart create(Cart cart);

}
