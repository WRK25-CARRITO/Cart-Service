package com.gft.wrk2025carrito.shopping_cart.application.service.client;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;

import java.util.List;
import java.util.Map;

public interface ProductMicroserviceService {

    public Map<Long,Product> getProductsFromCart(Cart cart);

    public List<Promotion> getAllApplicablePromotions(Cart cart);

}
