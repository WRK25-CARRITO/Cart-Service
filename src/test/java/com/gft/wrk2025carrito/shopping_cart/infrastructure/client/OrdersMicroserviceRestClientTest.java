package com.gft.wrk2025carrito.shopping_cart.infrastructure.client;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrdersMicroserviceRestClientTest {

    private RestTemplate restTemplate;
    private OrdersMicroserviceRestClient client;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        client = new OrdersMicroserviceRestClient(restTemplate);
        client.url = "http://localhost:8080/offers";
    }

    @Test
    void getAllOrderPromotions_success() {
        CartDetail d1 = new CartDetail();
        d1.setProductId(100L);
        d1.setQuantity(2);

        CartDetail d2 = new CartDetail();
        d2.setProductId(200L);
        d2.setQuantity(5);

        Cart cart = new Cart();
        cart.setCartDetails(List.of(d1, d2));

        List<Long> fakePromos = List.of(10L, 20L, 30L);
        ResponseEntity<List<Long>> response =
                new ResponseEntity<>(fakePromos, HttpStatus.OK);

        when(restTemplate.exchange(
                argThat((String uri) ->
                        uri.startsWith("http://localhost:8080/offers?") &&
                                uri.contains("100=2") &&
                                uri.contains("200=5")
                ),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<Long> result = client.getAllOrderPromotions(cart);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(List.of(10L, 20L, 30L), result);
    }

    @Test
    void getAllOrderPromotions_errorOnStatus() {
        CartDetail d = new CartDetail();
        d.setProductId(42L);
        d.setQuantity(1);

        Cart cart = new Cart();
        cart.setCartDetails(List.of(d));

        ResponseEntity<List<Long>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.exchange(
                argThat((String uri) ->
                        uri.startsWith("http://localhost:8080/offers?") &&
                                uri.contains("42=1")
                ),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.getAllOrderPromotions(cart)
        );
        assertTrue(ex.getMessage().startsWith("Error fetching applicable promotions"));
        assertTrue(ex.getMessage().contains("404"));
    }

    @Test
    void getAllOrderPromotions_nullBody() {
        CartDetail d = new CartDetail();
        d.setProductId(7L);
        d.setQuantity(3);

        Cart cart = new Cart();
        cart.setCartDetails(List.of(d));

        ResponseEntity<List<Long>> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                argThat((String uri) ->
                        uri.startsWith("http://localhost:8080/offers?") &&
                                uri.contains("7=3")
                ),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.getAllOrderPromotions(cart)
        );
        assertEquals("No promotions can be applied", ex.getMessage());
    }

    @Test
    void getAllOrderPromotions_emptyCart_ThrowsIllegalArgumentException() {
        Cart cart = new Cart();
        cart.setCartDetails(Collections.emptyList());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> client.getAllOrderPromotions(cart)
        );
        assertEquals("Cart with id " + cart.getId() + " is empty", ex.getMessage());
    }

}
