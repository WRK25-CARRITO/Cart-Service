package com.gft.wrk2025carrito.shopping_cart.application.service.impl;

import com.gft.wrk2025carrito.shopping_cart.application.service.CartService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartJpaRepository cartRepository;

    public CartServiceImpl(CartJpaRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Transactional
    @Override
    public CartEntity update(Cart cart){
        if(cart.getId().id() == null) {
            throw new IllegalArgumentException("The cart must have an id");
        }

        CartEntity cartEntity = cartRepository.findById(cart.getId().id()).orElse(null);
        if(cartEntity == null){
            throw new IllegalArgumentException("The cart does not exist");
        }

        Cart existingCart = CartFactory.toDomain(cartEntity);
        if(cart.equals(existingCart)){
            throw new IllegalArgumentException("No changes were made");
        }

        return cartRepository.save(CartFactory.toEntity(cart));

    }

}
