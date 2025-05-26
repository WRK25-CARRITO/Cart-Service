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
    void should_Return204_WhenDeletingCartById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/carts/" + id))
                .andExpect(status().isNoContent());

        verify(cartServices).delete(id);
    }

    @Test
    void should_Return400_WhenDeletingCartById() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalArgumentException()).when(cartServices).delete(id);

        mockMvc.perform(delete("/carts/" + id))
                .andExpect(status().isBadRequest());

    }

    @Test
    void should_Return404_WhenCart_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalStateException()).when(cartServices).delete(id);

        mockMvc.perform(delete("/carts/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_Return204_WhenDeletingCartByUserId() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/carts/user/" + id))
                .andExpect(status().isNoContent());

        verify(cartServices).deleteAllByUserId(id);
    }

    @Test
    void should_Return400_WhenDeletingCartByUserId() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalArgumentException()).when(cartServices).deleteAllByUserId(id);

        mockMvc.perform(delete("/carts/user/" + id))
                .andExpect(status().isBadRequest());

    }

    @Test
    void should_Return404_WhenCartByUser_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new IllegalStateException()).when(cartServices).deleteAllByUserId(id);

        mockMvc.perform(delete("/carts/user/" + id))
                .andExpect(status().isNotFound());
    }

}
