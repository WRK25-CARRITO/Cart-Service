package com.gft.wrk2025carrito.shopping_cart.domain.model;

import org.springframework.util.Assert;

import java.util.UUID;

public record CountryTaxId(UUID id) {

    public CountryTaxId {
        Assert.notNull(id, "id must not be null");
    }

    public CountryTaxId() {
        this(UUID.randomUUID());
    }

}
