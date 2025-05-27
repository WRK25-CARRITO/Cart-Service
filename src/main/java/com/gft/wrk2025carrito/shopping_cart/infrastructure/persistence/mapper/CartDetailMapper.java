package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;
import org.springframework.stereotype.Service;

@Service
public class CartDetailMapper {

    public CartDetailEntity toEntity(CartDetail cartDetailDomain) {

        if(cartDetailDomain == null) return null;

        return CartDetailEntity.builder()
                .productId(cartDetailDomain.getProductId())
                .quantity(cartDetailDomain.getQuantity())
                .totalPrice(cartDetailDomain.getTotalPrice())
                .totalWeight(cartDetailDomain.getTotalWeight())
                .build();
    }

    public CartDetail toDomain(CartDetailEntity cartDetailEntity) {

        if(cartDetailEntity == null) return null;

        return CartDetail.build(
                cartDetailEntity.getProductId(),
                cartDetailEntity.getQuantity(),
                cartDetailEntity.getTotalPrice(),
                cartDetailEntity.getTotalWeight()
                );
    }

}
