package com.gft.wrk2025carrito.shopping_cart.application.dto;

import java.util.Map;
import java.util.UUID;

public record CartUpdateDTO(
    UUID cartId,
    Map<UUID, Integer> productData
){}
