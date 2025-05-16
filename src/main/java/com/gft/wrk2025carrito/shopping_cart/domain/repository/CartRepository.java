package com.gft.wrk2025carrito.shopping_cart.domain.repository;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartId;

public interface CartRepository {

    Cart findById(CartId cartId);
}
