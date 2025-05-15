package com.gft.wrk2025carrito.shopping_cart.domain.model;

import org.springframework.util.Assert;

import java.util.UUID;

public record PaymentMethodId(UUID id) {

    public PaymentMethodId {
        Assert.notNull(id, "id must not be null");
    }

    public PaymentMethodId() {
        this(UUID.randomUUID());
    }
}
