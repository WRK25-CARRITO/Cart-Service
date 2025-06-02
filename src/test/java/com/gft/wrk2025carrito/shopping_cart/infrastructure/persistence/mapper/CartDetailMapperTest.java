package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CartDetailMapperTest {

    private final Random random = new Random();

    private final CartDetailMapper cartDetailMapper = new CartDetailMapper();

    @Test
    void toEntity_and_back_should_preserve_values() {
        CartDetail domain = CartDetail.build(
                random.nextLong(),
        2,
                BigDecimal.valueOf(10.5),
                1.2
        );

        CartDetailEntity entity = cartDetailMapper.toEntity(domain);
        assertEquals(domain.getProductId(), entity.getProductId());
        assertEquals(domain.getQuantity(), entity.getQuantity());

        CartDetail result = cartDetailMapper.toDomain(entity);
        assertEquals(domain.getProductId(), result.getProductId());
        assertEquals(domain.getQuantity(), result.getQuantity());
    }

    @Test
    void toEntity_should_return_null_if_input_is_null() {
        assertNull(cartDetailMapper.toEntity(null));
    }

    @Test
    void toDomain_should_return_null_if_input_is_null() {
        assertNull(cartDetailMapper.toDomain(null));
    }
}
