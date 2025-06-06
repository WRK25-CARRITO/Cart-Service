package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.PaymentMethodMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartEntityJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartEntityRepositoryImplTest {

    @Mock
    private CartEntityJpaRepository jpaRepository;

    @Mock
    private CartFactory cartFactory;

    @Mock
    private CartDetailMapper cartDetailMapper;

    @Mock
    private PaymentMethodMapper  paymentMethodMapper;

    @Mock
    private CountryTaxMapper countryTaxMapper;

    @InjectMocks
    private CartEntityRepositoryImpl repository;

    private final UUID userId = UUID.randomUUID();
    private final UUID cartId = UUID.randomUUID();


    @Test
    void should_getAllCarts(){
        repository.findAll();
        verify(jpaRepository).findAll();
    }

    @Test
    void should_DeleteById() {
        repository.deleteById(cartId);
        verify(jpaRepository).deleteById(cartId);
    }

    @Test
    void should_getAllActiveCarts(){
        repository.findAllActive();
        verify(jpaRepository).findByState(CartState.ACTIVE);
    }


    @Test
    void should_ReturnTrue_WhenCartExists() {
        when(jpaRepository.existsById(cartId)).thenReturn(true);

        boolean result = repository.existsById(cartId);

        assertTrue(result);
        verify(jpaRepository).existsById(cartId);
    }

    @Test
    void should_ReturnFalse_WhenCartDoesNotExist() {
        when(jpaRepository.existsById(cartId)).thenReturn(false);

        boolean result = repository.existsById(cartId);

        assertFalse(result);
        verify(jpaRepository).existsById(cartId);
    }

    @Test
    void should_ReturnNull_whenCart_IsNotFound() {
        when(jpaRepository.findById(cartId)).thenReturn(Optional.empty());

        Cart result = repository.findById(cartId);

        assertNull(result);
        verify(jpaRepository).findById(cartId);
        verifyNoInteractions(cartFactory);
    }

    @Test
    void should_ReturnCart_WhenCartExists() {

        CartEntity cartEntity = new CartEntity();
        Cart cart = mock(Cart.class);

        when(jpaRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
        when(cartFactory.toDomain(cartEntity)).thenReturn(cart);

        Cart result = repository.findById(cartId);
        assertNotNull(result);
        assertEquals(cart, result);
        verify(jpaRepository).findById(cartId);
    }

    @Test
    void should_FindByUserIdAndMapToDomain() {
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
    void should_DeleteAllByUserId() {
        repository.deleteAllByUserId(userId);
        verify(jpaRepository).deleteAllByUserId(userId);
    }

    @Test
    void should_SaveCartEntity() {
        CartEntity entity = new CartEntity();
        Cart expectedCart = mock(Cart.class);

        when(jpaRepository.save(entity)).thenReturn(entity);
        when(cartFactory.toDomain(entity)).thenReturn(expectedCart);

        Cart result = repository.save(entity);

        assertEquals(expectedCart, result);
    }


    @Test
    void should_true_cartExists_ByUserId_AndStateActive() {
        UUID userIdWithCart = UUID.fromString("2f05a6f9-87dc-4ea5-a23c-b05265055334");
        when(jpaRepository.existsByUserIdAndState(userIdWithCart, CartState.ACTIVE)).thenReturn(true);
        boolean prueba = repository.cartExistsByUserIdAndStateActive(userIdWithCart);

        assertTrue(prueba);


    }

    @Test
    void should_false_cartDoesNotExistsByUserIdAndStateActiveTest() {
        UUID userFalseId = UUID.fromString("11101c19-0f41-4e17-8567-474937f6ca42");
        when(jpaRepository.existsByUserIdAndState(userFalseId, CartState.ACTIVE)).thenReturn(false);

        boolean pruebaError = repository.cartExistsByUserIdAndStateActive(userFalseId);

        assertFalse(pruebaError);

    }

    @Test
    void should_SaveCartEntity_AndReturnDomainCart() {

        Cart domainCart = mock(Cart.class);
        CartEntity entityCart = mock(CartEntity.class);
        CartEntity savedEntityCart = mock(CartEntity.class);
        Cart expectedCart = mock(Cart.class);

        when(cartFactory.toEntity(domainCart)).thenReturn(entityCart);
        when(jpaRepository.save(entityCart)).thenReturn(savedEntityCart);
        when(cartFactory.toDomain(savedEntityCart)).thenReturn(expectedCart);

        Cart result = repository.create(domainCart);

        assertEquals(expectedCart, result);
        verify(cartFactory).toEntity(domainCart);
        verify(jpaRepository).save(entityCart);
        verify(cartFactory).toDomain(savedEntityCart);
    }


}

