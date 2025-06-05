package com.gft.wrk2025carrito.shopping_cart.application.service.client;

import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderMicroserviceService {

    List<Long> getAllOrderPromotions(Cart cart);

    UUID sendAOrder(OrderDTO order);
}
