package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor()
public class CartEntityRepositoryImpl implements CartRepository {

    private final CartEntityJpaRepository jpaRepository;
    private final CartFactory cartFactory;
    private final CartEntityJpaRepository cartEntityJpaRepository;

    public boolean existsByCartId(CartId cartId) {
        return cartEntityJpaRepository.existsById(cartId.id());
    }

    public CartEntity save(CartEntity newCartEntity) {
        return cartEntityJpaRepository.save(newCartEntity);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Cart> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(cartFactory::toDomain)
                .toList();
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        jpaRepository.deleteAllByUserId(userId);
    }
}
