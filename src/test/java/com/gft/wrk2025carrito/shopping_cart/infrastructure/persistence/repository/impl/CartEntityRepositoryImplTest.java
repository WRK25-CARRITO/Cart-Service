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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void cartExistsByUserIdAndStateActiveTest() {

        boolean prueba1 = cartEntityRepository.cartExistsByUserIdAndStateActive(UUID.fromString ("2f05a6f9-87dc-4ea5-a23c-b05265055334"));
        boolean prueba2 = cartEntityRepository.cartExistsByUserIdAndStateActive(UUID.fromString("11101c19-0f41-4e17-8567-474937f6ca42"));

        assertTrue(prueba1);
        assertFalse(prueba2);

    }

    @Test
    void create() {
        CartId cartID = new CartId();
        UUID userId = UUID.fromString("2f05a6f9-87dc-4ea5-a23c-b05265055334");
        Cart cart = Cart.build(cartID, userId, null, null, null, null, new Date(), null, null, CartState.ACTIVE, null);

        cart.setCreatedAt(new Date());

        cartEntityRepository.create(cart);

    }

}

