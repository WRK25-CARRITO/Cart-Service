package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartServices cartServices;

    @InjectMocks
    private CartController controller;

    @Test
    void getAllCarts() {
        controller.getAll();
        verify(cartServices, atLeastOnce()).getAll();
    }

    @Test
    void getCartById_ok() {
        UUID id = UUID.randomUUID();
        controller.getById(id);
        verify(cartServices, atLeastOnce()).getById(id);
    }

    @Test
    void should_DeleteCartById_Successfully() {
        UUID id = UUID.randomUUID();
        controller.delete(id);
        verify(cartServices).delete(id);
    }

    @Test
    void should_DeleteCartByUserId_Successfully() {
        UUID userId = UUID.randomUUID();
        controller.deleteByUser(userId);
        verify(cartServices).deleteAllByUserId(userId);
    }

    @Test
    void should_UpdateCart_Successfully() {
        UUID cartId = UUID.randomUUID();
        Map<Long, Integer> cartProducts = Map.of(1L, 2, 2L, 3);
        controller.update(cartId, cartProducts);
        verify(cartServices).update(cartId, cartProducts);
    }

    @Test
    void should_CreateCart_Successfully() {
        UUID id = UUID.randomUUID();
        controller.createCart(id);
        verify(cartServices).createCart(id);
    }

    @Test
    void should_UpdateCartState_Successfully() {
        UUID cartId = UUID.randomUUID();
        CartDTO dto = new CartDTO(CartState.PENDING, null, null);

        Cart dummyCart = mock(Cart.class);
        when(cartServices.updateState(cartId, dto)).thenReturn(dummyCart);

        Cart result = controller.updateState(cartId, dto);

        verify(cartServices, times(1)).updateState(cartId, dto);
        assertEquals(dummyCart, result);
    }

}
