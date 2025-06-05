package com.gft.wrk2025carrito.shopping_cart.infrastructure.dto;

import java.util.UUID;

public record UserProductStockChangedNotificationDTO(UUID userId, Long productId) {
}
