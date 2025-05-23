package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor()
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository cartJpaRepository;

    public boolean existsByCartId(CartId cartId) {
        return cartJpaRepository.existsById(cartId.id());
    }

    public Object save(CartEntity newCartEntity) {
        return cartJpaRepository.save(newCartEntity);
    }
}
