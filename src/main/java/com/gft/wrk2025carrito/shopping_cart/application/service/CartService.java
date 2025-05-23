package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import jakarta.transaction.Transactional;

public interface CartService {

    @Transactional
    CartEntity update(Cart cart);


}
