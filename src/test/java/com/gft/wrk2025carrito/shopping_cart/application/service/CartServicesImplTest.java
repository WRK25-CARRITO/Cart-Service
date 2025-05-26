package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.application.service.impl.CartServicesImpl;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartEntityRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServicesImplTest {

    @Mock
    private CartEntityRepositoryImpl repository;

    @InjectMocks
    private CartServicesImpl service;

    @Test
    void should_throw_exception_when_id_is_null() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(null));
    }

    @Test
    void should_throw_exception_when_cart_not_exists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> service.delete(id));
    }

    @Test
    void should_delete_cart_when_exists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void update_Cart_shouldOK() {

        CartId cartId = new CartId(UUID.randomUUID());
        UUID userUUID = UUID.randomUUID();

        Cart updatedIncomingCart = Cart.build(
                cartId,
                userUUID,
                null,
                null ,
                BigDecimal.valueOf(10.0),
                5.0,
                new Date(),
                new Date(),
                Collections.emptyList(),
                CartState.ACTIVE,
                Collections.emptyList());

        CartEntity updatedIncomingCartEntity = new CartEntity(
                cartId.id(),
                userUUID,
                5.0,
                null ,
                null,
                BigDecimal.valueOf(10.0),
                CartState.ACTIVE,
                new Date(),
                new Date(),
                Collections.emptyList(),
                Collections.emptyList());

        when(repository.existsById(cartId.id())).thenReturn(true);
        when(repository.save(updatedIncomingCartEntity)).thenReturn(updatedIncomingCartEntity);

        cartServiceImpl.update(updatedIncomingCart);

        verify(repository).save(updatedIncomingCartEntity);

    }
}