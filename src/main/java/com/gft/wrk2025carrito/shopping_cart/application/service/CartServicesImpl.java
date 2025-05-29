package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServicesImpl implements CartServices {

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) throw new IllegalArgumentException("Cart ID must not be null");

        if (!cartRepository.existsById(id)) throw new IllegalStateException("No cart found with ID " + id);

        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("User ID must not be null");

        List<Cart> carts = cartRepository.findByUserId(userId);

        if (carts.isEmpty()) throw new IllegalStateException("No carts found for user with ID: " + userId);

        cartRepository.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    public Cart createCart(UUID userId) {

        if(userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        boolean exists = cartRepository.cartExistsByUserIdAndStateActive(userId);

        if (exists){
            throw new IllegalArgumentException(" An active cart already exists for user");
        }

        Cart cart = Cart.build(new CartId(), userId, null, null, null, null, new Date(), null, java.util.Collections.emptyList(), CartState.ACTIVE, java.util.Collections.emptyList());

        return cartRepository.create(cart);
    }

}
