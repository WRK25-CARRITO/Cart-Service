package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartServices cartServices;

    @Test
    void testShouldReturn204WhenDeleted() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/carts/" + id))
                .andExpect(status().isNoContent());

        verify(cartServices).delete(id);
    }

    @Test
    void testShouldReturn400WhenIllegalArgument() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalArgumentException()).when(cartServices).delete(id);

        mockMvc.perform(delete("/carts/" + id))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testShouldReturn404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalStateException()).when(cartServices).delete(id);

        mockMvc.perform(delete("/carts/" + id))
                .andExpect(status().isNotFound());
    }
}
