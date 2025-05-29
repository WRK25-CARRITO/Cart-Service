package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

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
    void should_DeleteCartByUserId_Successfully() {
        UUID userId = UUID.randomUUID();

        controller.deleteByUser(userId);

        verify(cartServices).deleteAllByUserId(userId);
    }

    @Test
    void should_CreateCart_Successfully() {
        UUID id = UUID.randomUUID();
        controller.createCart(id);
        verify(cartServices).createCart(id);
    }

}
