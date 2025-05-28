package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartEntityRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServicesImplTest {

    @Mock
    private CartEntityRepositoryImpl repository;

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
    void update_Cart_shouldOK() {

//        Map<UUID, Integer> products = new HashMap<>();
//        CartUpdateDTO incomingUpdateData = new CartUpdateDTO(UUID.randomUUID(), products);
//        UUID id = UUID.randomUUID();

        CartEntity cartEntity = new CartEntity();

        when(repository.save()).thenReturn(true);



    }
}