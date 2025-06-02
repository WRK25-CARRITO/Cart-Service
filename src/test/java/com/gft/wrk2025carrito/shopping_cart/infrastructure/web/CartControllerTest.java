package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartUpdateDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

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
        CartUpdateDTO dto = mock(CartUpdateDTO.class);
        controller.update(dto);
        verify(cartServices).update(dto);
    }


    @Test
    void should_CreateCart_Successfully() {
        UUID id = UUID.randomUUID();
        controller.createCart(id);
        verify(cartServices).createCart(id);
    }

}
