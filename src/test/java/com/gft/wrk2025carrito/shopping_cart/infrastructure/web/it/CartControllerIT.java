package com.gft.wrk2025carrito.shopping_cart.infrastructure.web.it;

import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartEntityJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// sin esto no funciona
@Sql(scripts = {"/data/h2/schema.sql", "/data/h2/data.sql"})
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartEntityJpaRepository cartRepository;

    private UUID existingCartId;
    private UUID existingUserId;

    @BeforeEach
    void setUp() {
        existingCartId = UUID.fromString("4d82b684-7131-4ba4-864d-465fc290708b");
        existingUserId = UUID.fromString("b96124a9-69a6-4859-acc7-5708ab07cd80");
        assertFalse(cartRepository.findByUserId(existingUserId).isEmpty());
        assertTrue(cartRepository.existsById(existingCartId));
    }

    @Test
    void should_Return204_AndDeleteCartById_WhenExists() throws Exception {
        mockMvc.perform(delete("/carts/" + existingCartId))
                .andExpect(status().isNoContent());

        assertFalse(cartRepository.existsById(existingCartId));
    }

    @Test
    void should_Return404_WhenDeletingNonExistentCartById() throws Exception {
        UUID nonexistentId = UUID.randomUUID();

        mockMvc.perform(delete("/carts/" + nonexistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_Return204_AndDeleteAllCartsByUser_WhenExists() throws Exception {
        mockMvc.perform(delete("/carts/user/" + existingUserId))
                .andExpect(status().isNoContent());

        assertTrue(cartRepository.findByUserId(existingUserId).isEmpty());
    }

    @Test
    void should_Return404_WhenDeletingCartsByUserIdThatHasNoCarts() throws Exception {
        UUID noCartUserId = UUID.randomUUID();

        mockMvc.perform(delete("/carts/user/" + noCartUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_Return400_WhenDeletingCartByInvalidId() throws Exception {
        mockMvc.perform(delete("/carts/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_WhenDeletingByUserIdInvalidUUID() throws Exception {
        mockMvc.perform(delete("/carts/user/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return201_WhenCreatingCartSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/carts")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void should_Return400_WhenCreatingCartWithInvalidUUID() throws Exception {
        mockMvc.perform(post("/carts")
                        .param("userId", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_Return400_WhenCartAlreadyExistsInActive() throws Exception {
        UUID userId = UUID.fromString("2f05a6f9-87dc-4ea5-a23c-b05265055334");

        mockMvc.perform(post("/carts")
                        .param("userId", userId.toString()))
                .andExpect(status().isBadRequest());
    }

}
