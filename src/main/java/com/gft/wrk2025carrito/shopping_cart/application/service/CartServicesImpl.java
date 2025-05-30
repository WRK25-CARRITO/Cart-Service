package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartUpdateDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.math.BigDecimal;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServicesImpl implements CartServices {

    private final CartRepository cartRepository;
    private final CartFactory cartFactory;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) throw new IllegalArgumentException("Cart ID must not be null");

        if (!cartRepository.existsById(id)) throw new IllegalStateException("No cart found with ID " + id);

        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(CartUpdateDTO cartDTO) {
        String url = "https://workshop-7uvd.onrender.com/api/v1/products/list-by-ids";

        if (cartDTO.cartId() == null) {
            throw new IllegalArgumentException("Cart id cannot be null");
        }

        if (!cartRepository.existsById(cartDTO.cartId())) {
            throw new IllegalArgumentException("Cart with id " + cartDTO.cartId() + " does not exist");
        }

        if(cartRepository.findById(cartDTO.cartId()).getState() == CartState.PENDING || cartRepository.findById(cartDTO.cartId()).getState() == CartState.CLOSED) {
            throw new IllegalArgumentException("Cart with id " + cartDTO.cartId() + " cannot be updated");
        }

        // eliminar duplicados y sumar cantidad
        Map<Long, Integer> productQuantities = cartDTO.productData().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));

        List<Long> productIds = new ArrayList<>(productQuantities.keySet());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> request = new HttpEntity<>(productIds, headers);

        System.out.println("Request enviada ");

        // llamada a la API de productos
        ResponseEntity<Object[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Object[].class
        );

        Object[] productos = response.getBody();

        System.out.println("Request terminada ");

        if (productos == null || productos.length != productIds.size()) {
            throw new IllegalArgumentException("One or more product IDs are invalid");
        }

        List<CartDetail> newCartDetails = cartDTO.productData().entrySet().stream()
                .map(entry -> CartDetail.build(entry.getKey(), entry.getValue(), BigDecimal.ZERO, 0.0))
                .toList();

        Cart cart = cartRepository.findById(cartDTO.cartId());
        cart.setCartDetails(newCartDetails);
        cartRepository.save(cartFactory.toEntity(cart));
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
