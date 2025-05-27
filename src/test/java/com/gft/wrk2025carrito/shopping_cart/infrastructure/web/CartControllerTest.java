package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartServices cartServices;

    @InjectMocks
    private CartController controller;

    @Test
    void should_DeleteCartById_Successfully() {
        UUID id = UUID.randomUUID();

        controller.delete(id);

        verify(cartServices).delete(id);
    }

    @Test
    void should_Throw400_WhenDeletingCartByIdWithInvalidInput() {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalArgumentException()).when(cartServices).delete(id);

        assertThrows(IllegalArgumentException.class, () -> controller.delete(id));
    }

    @Test
    void should_Throw404_WhenCartByIdNotFound() {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalStateException()).when(cartServices).delete(id);

        assertThrows(IllegalStateException.class, () -> controller.delete(id));
    }

    @Test
    void should_DeleteCartByUserId_Successfully() {
        UUID userId = UUID.randomUUID();

        controller.deleteByUser(userId);

        verify(cartServices).deleteAllByUserId(userId);
    }

    @Test
    void should_Throw400_WhenDeletingCartByUserIdWithInvalidInput() {
        UUID userId = UUID.randomUUID();
        doThrow(new IllegalArgumentException()).when(cartServices).deleteAllByUserId(userId);

        assertThrows(IllegalArgumentException.class, () -> controller.deleteByUser(userId));
    }

    @Test
    void should_Throw404_WhenCartByUserIdNotFound() {
        UUID userId = UUID.randomUUID();
        doThrow(new IllegalStateException()).when(cartServices).deleteAllByUserId(userId);

        assertThrows(IllegalStateException.class, () -> controller.deleteByUser(userId));
    }

}
