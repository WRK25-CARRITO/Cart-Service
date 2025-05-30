package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartUpdateDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartEntityRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServicesImplTest {

    @Mock
    private CartEntityRepositoryImpl repository;

    @Mock
    private CartFactory cartFactory;

    @InjectMocks
    private CartServicesImpl cartService;

    @Test
    void should_throwException_whenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> cartService.delete(null));
    }

    @Test
    void should_throwException_whenCartNotExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> cartService.delete(id));
    }

    @Test
    void should_deleteCart_whenExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        cartService.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void should_throwException_whenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> cartService.deleteAllByUserId(null));
    }

    @Test
    void should_throwException_whenCartByUser_notExists() {
        UUID id = UUID.randomUUID();
        when(repository.findByUserId(id)).thenReturn(Collections.emptyList());

        assertThrows(IllegalStateException.class, () -> cartService.deleteAllByUserId(id));
    }

    @Test
    void should_deleteCartByUser_whenExists() {
        UUID id = UUID.randomUUID();
        List<Cart> carts = List.of(mock(Cart.class));
        when(repository.findByUserId(id)).thenReturn(carts);

        cartService.deleteAllByUserId(id);

        verify(repository, times(1)).deleteAllByUserId(id);
    }

    @Test
    void should_updateCart_whenExists() {
        UUID cartId = UUID.randomUUID();
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        CartUpdateDTO dto = new CartUpdateDTO(cartId, productData);
        Cart existingCart = mock(Cart.class);

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(existingCart);
        when(repository.save(any())).thenReturn(existingCart);

        cartService.update(dto);

        verify(existingCart).setCartDetails(any());
        verify(repository).save(any());
    }



    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.createCart(null);
        });
        assertEquals("User ID must not be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfActiveCartExists() {
        UUID userId = UUID.randomUUID();
        when(repository.cartExistsByUserIdAndStateActive(userId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.createCart(userId);
        });
        assertEquals(" An active cart already exists for user", exception.getMessage());
    }

    @Test
    void shouldCreateCartIfNoneExists() {
        UUID userId = UUID.randomUUID();
        when(repository.cartExistsByUserIdAndStateActive(userId)).thenReturn(false);

        Cart expectedCart = Cart.build(
                new CartId(), userId, null, null, null, null,
                new Date(), null,
                java.util.Collections.emptyList(),
                CartState.ACTIVE,
                java.util.Collections.emptyList()
        );

        when(repository.create(any(Cart.class))).thenReturn(expectedCart);

        Cart result = cartService.createCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(CartState.ACTIVE, result.getState());
        verify(repository).create(any(Cart.class));
    }

}