package com.gft.wrk2025carrito.shopping_cart.application.service.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServicesImpl implements CartServices {

    private final CartRepository cartRepository;
    private final CartFactory cartFactory;

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) throw new IllegalArgumentException("Cart ID must not be null");

        if (!cartRepository.existsById(id)) throw new IllegalStateException("Cart not found");

        cartRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(Cart cart){

        if(cart.getId() == null){
            throw new IllegalArgumentException("Cart id cannot be null");
        }

        if(!cartRepository.existsById(cart.getId().id())){
            throw new IllegalArgumentException("Cart with id " + cart.getId().id() + " does not exist");
        }

        CartEntity cartEntity = cartFactory.toEntity(cart);
        cartRepository.save(cartEntity);

    }



}
