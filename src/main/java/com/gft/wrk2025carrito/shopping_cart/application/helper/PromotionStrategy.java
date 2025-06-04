package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.math.BigDecimal;
import java.util.Map;

public interface PromotionStrategy {

    boolean supports(Promotion promotion);
    BigDecimal apply(Promotion promotion, Cart cart, Map<Long, Product> productMap);

}