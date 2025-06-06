package com.gft.wrk2025carrito.shopping_cart.application.dto;

import java.util.UUID;

public record OrderDTO(
        UUID userId,
        UUID cartId,
        String state
) {
}
