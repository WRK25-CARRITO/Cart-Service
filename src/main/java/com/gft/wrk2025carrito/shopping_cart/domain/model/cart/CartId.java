package com.gft.wrk2025carrito.shopping_cart.domain.model.cart;

import org.springframework.util.Assert;

import java.util.UUID;

public record CartId(UUID id ) {

    public CartId {
        Assert.notNull(id, "id must not be null");
    }

    public CartId() {
        this(UUID.randomUUID());
    }

}
