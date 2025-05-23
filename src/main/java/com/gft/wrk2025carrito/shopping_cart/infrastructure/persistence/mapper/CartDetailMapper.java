package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;

import java.math.BigDecimal;

public class CartDetailMapper {

    public static CartDetailEntity toEntity(CartDetail cartDetailDomain) {

        return CartDetailEntity.builder()
                .productId(cartDetailDomain.getProductId())
                .quantity(cartDetailDomain.getQuantity())
                .totalPrice(cartDetailDomain.getTotalPrice())
                .totalWeight(cartDetailDomain.getTotalWeight())
                .build();
    }

    public static CartDetail toDomain(CartDetailEntity cartDetailEntity) {

        return CartDetail.build(
                cartDetailEntity.getProductId(),
                cartDetailEntity.getQuantity(),
                cartDetailEntity.getTotalPrice(),
                cartDetailEntity.getTotalWeight()
        );

    }

}
