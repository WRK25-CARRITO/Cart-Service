package com.gft.wrk2025carrito.shopping_cart.application.service.client;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.util.List;
import java.util.Map;

public interface OrderMicroserviceService {

    List<Long> getAllOrderPromotions(Cart cart);
}
