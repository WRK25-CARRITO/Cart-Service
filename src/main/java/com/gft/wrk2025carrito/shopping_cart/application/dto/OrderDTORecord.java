package com.gft.wrk2025carrito.shopping_cart.application.dto;

import java.util.UUID;

public record OrderDTORecord(UUID userId,
                             UUID cartId,
                             String state) {
}
