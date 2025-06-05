package com.gft.wrk2025carrito.shopping_cart.infrastructure.dto;

import org.springframework.util.Assert;

public record StockNotificationResponse(Long productId, Integer stock) {
    public StockNotificationResponse {
        Assert.notNull(productId, "The product id must not be null");
        Assert.notNull(stock, "The stock must not be null");
    }
}
