package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;

import java.math.BigDecimal;

public class CartDetailMapper {
    public static CartDetailEntity toEntity(CartDetail cartDomain) {
        return CartDetailEntity.builder()
                .productId(cartDomain.getProductId())
                .quantity(cartDomain.getQuantity())
                .totalPrice(BigDecimal.valueOf(cartDomain.getPrice()))
                .totalWeight(cartDomain.getWeight())
                .build();
    }

    public static CartDetail toDomain(CartDetailEntity cartEntity) {
        return CartDetail.build(
                cartEntity.getProductId(),
                cartEntity.getQuantity(),
                cartEntity.getTotalPrice().toBigInteger().doubleValue(),
                cartEntity.getTotalWeight()
        );

    }
}
