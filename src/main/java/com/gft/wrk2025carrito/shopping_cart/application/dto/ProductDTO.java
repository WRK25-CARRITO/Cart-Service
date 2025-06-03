package com.gft.wrk2025carrito.shopping_cart.application.dto;

import java.math.BigDecimal;

public record ProductDTO(
    Long id,
    BigDecimal price,
    Double weight
) {
}
