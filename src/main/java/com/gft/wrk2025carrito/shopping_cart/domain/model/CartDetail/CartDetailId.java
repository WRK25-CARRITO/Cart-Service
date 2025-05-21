package com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.UUID;

public record CartDetailId(UUID id){

    public CartDetailId{
        Assert.notNull(id, "id cannot be null");
    }

    public CartDetailId(){ this(UUID.randomUUID()); }
}
