package com.gft.wrk2025carrito.shopping_cart.infrastructure.web.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.wrk2025carrito.shopping_cart.application.dto.CartDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import com.gft.wrk2025carrito.shopping_cart.application.service.client.OrderMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.messages.CartProducer;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartEntityJpaRepository;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Import(CartControllerIT.StubOrderServiceConfig.class)
@ActiveProfiles("test")
@Sql(scripts = {"/data/h2/schema.sql", "/data/h2/data.sql"})
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartEntityJpaRepository cartRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartProducer cartProducer;

    private UUID activeCartId;
    private UUID pendingCartId;
    private UUID existingUserId;

    @TestConfiguration
    static class StubOrderServiceConfig {
        @Bean
        @Primary
        public OrderMicroserviceService orderMicroserviceService() {
            return new OrderMicroserviceService() {
                @Override
                public List<Long> getAllOrderPromotions(Cart cart) {
                    return Collections.emptyList();
                }

                @Override
                public UUID sendAOrder(OrderDTO order) {
                    return UUID.randomUUID();
                }
            };
        }
    }

    @BeforeEach
    void setUp() {
        activeCartId = UUID.fromString("4d82b684-7131-4ba4-864d-465fc290708b");
        pendingCartId = UUID.fromString("bdbfb686-3fc4-4a3b-9f70-df76cdff0791");
        existingUserId = UUID.fromString("b96124a9-69a6-4859-acc7-5708ab07cd80");
        assertFalse(cartRepository.findByUserId(existingUserId).isEmpty());
        assertTrue(cartRepository.existsById(activeCartId));
    }

    @Test
    void should_Return204_whenGettingAllCarts() throws Exception {
        mockMvc.perform(get("/api/v1/carts")).andExpect(status().isOk());

        assertFalse(cartRepository.findAll().isEmpty());
    }

    @Test
    void should_Return204_whenGettingCartById() throws Exception {
        activeCartId = UUID.fromString("4d82b684-7131-4ba4-864d-465fc290708b");
        mockMvc.perform(get("/api/v1/carts/" + activeCartId)).andExpect(status().isOk());

        assertTrue(cartRepository.findById(activeCartId).isPresent());
    }

    @Test
    void should_Return204_AndDeleteCartById_WhenExists() throws Exception {
        mockMvc.perform(delete("/api/v1/carts/" + activeCartId))
                .andExpect(status().isNoContent());

        assertFalse(cartRepository.existsById(activeCartId));
    }

    @Test
    void should_Return404_WhenDeletingNonExistentCartById() throws Exception {
        UUID nonexistentId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/carts/" + nonexistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_Return204_AndDeleteAllCartsByUser_WhenExists() throws Exception {
        mockMvc.perform(delete("/api/v1/carts/user/" + existingUserId))
                .andExpect(status().isNoContent());

        assertTrue(cartRepository.findByUserId(existingUserId).isEmpty());
    }

    @Test
    void should_Return404_WhenDeletingCartsByUserIdThatHasNoCarts() throws Exception {
        UUID noCartUserId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/carts/user/" + noCartUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_Return400_WhenDeletingCartByInvalidId() throws Exception {
        mockMvc.perform(delete("/api/v1/carts/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_WhenDeletingByUserIdInvalidUUID() throws Exception {
        mockMvc.perform(delete("/api/v1/carts/user/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return201_WhenCreatingCartSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/carts")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void should_Return400_WhenCreatingCartWithInvalidUUID() throws Exception {
        mockMvc.perform(post("/api/v1/carts")
                        .param("userId", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_Return400_WhenCartAlreadyExistsInActive() throws Exception {
        UUID userId = UUID.fromString("2f05a6f9-87dc-4ea5-a23c-b05265055334");

        mockMvc.perform(post("/api/v1/carts")
                        .param("userId", userId.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_WhenUpdatingCartWithInvalidProductIds() throws Exception {
        Map<Long, Integer> invalidProducts = Map.of(999L, 1);

        String json = objectMapper.writeValueAsString(invalidProducts);

        mockMvc.perform(put("/api/v1/carts/{id}", activeCartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_WhenProductIdsAreInvalid() throws Exception {
        Map<Long, Integer> invalidProducts = Map.of(99999L, 1); // ID no válido

        String json = objectMapper.writeValueAsString(invalidProducts);

        mockMvc.perform(put("/api/v1/carts/{id}", activeCartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_WhenCartIdIsInvalid() throws Exception {
        Map<Long, Integer> validProducts = Map.of(1L, 1); // productos válidos

        String json = objectMapper.writeValueAsString(validProducts);

        mockMvc.perform(put("/api/v1/carts/{id}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_whenCartIdInvalid() throws Exception {
        mockMvc.perform(put("/api/v1/carts/confirm/invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_whenCartDoesNotExist() throws Exception {
        UUID randomId = UUID.randomUUID();
        CartDTO dto = new CartDTO(null, null, null);
        String bodyJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/v1/carts/confirm/" + randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_TransitionActiveToPending_andReturnUpdatedCart() throws Exception {
        cartRepository.findById(activeCartId).ifPresent(entity -> {
            entity.setState(CartState.ACTIVE);
            entity.setCountryTax(null);
            entity.setPaymentMethod(null);
            cartRepository.save(entity);
        });

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);
        String bodyJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                        put("/api/v1/carts/confirm/" + activeCartId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.id", is(activeCartId.toString())))
                .andExpect(jsonPath("$.state", is("PENDING")))
                .andExpect(jsonPath("$.totalPrice", notNullValue()))
                .andExpect(jsonPath("$.totalWeight", notNullValue()))
                .andExpect(jsonPath("$.promotionIds", isA(JSONArray.class)));
    }

    @Test
    void should_TransitionPendingToClosed_andReturnUpdatedCartWithTaxesAndCharges() throws Exception {
        CountryTax countryTax = CountryTax.build(
                new CountryTaxId(UUID.fromString("6d948074-3749-4329-9cab-0c87e3c1a7c5")),
                "Germany",
                0.19
        );
        PaymentMethod paymentMethod = PaymentMethod.build(
                new PaymentMethodId(UUID.fromString("41e05b0c-2340-4cdb-9d89-1524a8349f1c")),
                "PayPal",
                0.30
        );

        CartDTO dto = new CartDTO(CartState.CLOSED, countryTax, paymentMethod);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(
                        put("/api/v1/carts/confirm/" + pendingCartId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.id", is(pendingCartId.toString())))
                .andExpect(jsonPath("$.state", is("CLOSED")))
                .andExpect(jsonPath("$.totalPrice", notNullValue()))
                .andExpect(jsonPath("$.totalWeight", notNullValue()))
                .andExpect(jsonPath("$.countryTax.country", is("Germany")))
                .andExpect(jsonPath("$.paymentMethod.paymentMethod", is("PayPal")));
    }

    @Test
    void should_Return400_whenRequestBodyMissingRequiredFields() throws Exception {
        mockMvc.perform(put("/api/v1/carts/confirm/" + activeCartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return200_WhenGettingCalculatedCartById() throws Exception {
        mockMvc.perform(get("/api/v1/carts/calculated/" + activeCartId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id.id", is(activeCartId.toString())))
                .andExpect(jsonPath("$.totalPrice", is(95.73)))
                .andExpect(jsonPath("$.totalWeight", is(0.9)));

    }

    @Test
    void should_Return400_WhenCalculatedCartByInvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/carts/calculated/invalid-uuid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return400_WhenCalculatedCartNotFound() throws Exception {
        UUID nonexistentId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/carts/calculated/" + nonexistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
