package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor()
public class CartEntityRepositoryImpl implements CartRepository {

    private final CartEntityJpaRepository jpaRepository;
    private final CartFactory cartFactory;

    @Override
    public Cart save(CartEntity CartEntity) {
        CartEntity entity = jpaRepository.save(CartEntity);
        return cartFactory.toDomain(entity);
    }

    @Override
    public List<Cart> findAll() {
        return jpaRepository.findAll().stream().map(cartFactory::toDomain).collect(Collectors.toList());
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

    @Override
    public boolean cartExistsByUserIdAndStateActive(UUID userId) {
        return jpaRepository.existsByUserIdAndState(userId, CartState.ACTIVE);
    }

    @Override
    public Cart create(Cart cart) {
        CartEntity cartEntity = cartFactory.toEntity(cart);

        return cartFactory.toDomain(jpaRepository.save(cartEntity));
    }


    @Override
    public Cart findById(UUID id) {
        Optional<CartEntity> entity = jpaRepository.findById(id);

        return entity.map(cartFactory::toDomain).orElse(null);

    }
}
