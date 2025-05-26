package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor()
public class CartEntityRepositoryImpl implements CartRepository {

    private final CartEntityJpaRepository cartEntityJpaRepository;

    public boolean existsByCartId(CartId cartId) {
        return cartEntityJpaRepository.existsById(cartId.id());
    }

    public CartEntity save(CartEntity newCartEntity) {
        return cartEntityJpaRepository.save(newCartEntity);
    }

    @Override
    public void deleteById(UUID id) {
        cartEntityJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return cartEntityJpaRepository.existsById(id);
    }

}
