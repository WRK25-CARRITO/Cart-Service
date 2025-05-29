package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartEntityJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartEntityRepositoryImplTest {

    @Mock
    private CartEntityJpaRepository jpaRepository;

    @Mock
    private CartFactory cartFactory;

    @InjectMocks
    private CartEntityRepositoryImpl repository;

    private final UUID userId = UUID.randomUUID();
    private final UUID cartId = UUID.randomUUID();

    @Test
    void shouldDeleteById() {
        repository.deleteById(cartId);
        verify(jpaRepository).deleteById(cartId);
    }

    @Test
    void shouldReturnTrue_WhenCartExists() {
        when(jpaRepository.existsById(cartId)).thenReturn(true);

        boolean result = repository.existsById(cartId);

        assertTrue(result);
        verify(jpaRepository).existsById(cartId);
    }

    @Test
    void shouldReturnFalse_WhenCartDoesNotExist() {
        when(jpaRepository.existsById(cartId)).thenReturn(false);

        boolean result = repository.existsById(cartId);

        assertFalse(result);
        verify(jpaRepository).existsById(cartId);
    }

    @Test
    void shouldFindByUserIdAndMapToDomain() {
        CartEntity entity = new CartEntity();
        Cart domainCart = mock(Cart.class);
        when(jpaRepository.findByUserId(userId)).thenReturn(List.of(entity));
        when(cartFactory.toDomain(entity)).thenReturn(domainCart);

        List<Cart> result = repository.findByUserId(userId);

        assertTrue(result.contains(domainCart));
        verify(jpaRepository).findByUserId(userId);
        verify(cartFactory).toDomain(entity);
    }

    @Test
    void shouldDeleteAllByUserId() {
        repository.deleteAllByUserId(userId);
        verify(jpaRepository).deleteAllByUserId(userId);
    }

    @Test
    void shouldSaveCartEntity() {
        CartEntity entity = new CartEntity();
        Cart expectedCart = mock(Cart.class);

        when(jpaRepository.save(entity)).thenReturn(entity);
        when(cartFactory.toDomain(entity)).thenReturn(expectedCart);

        Cart result = repository.save(entity);

        assertEquals(expectedCart, result);
    }

}
