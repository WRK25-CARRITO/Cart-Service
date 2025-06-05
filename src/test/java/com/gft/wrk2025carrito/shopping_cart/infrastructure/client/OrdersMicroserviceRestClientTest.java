package com.gft.wrk2025carrito.shopping_cart.infrastructure.client;

import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrdersMicroserviceRestClientTest {

    private RestTemplate restTemplate;
    private OrdersMicroserviceRestClient client;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        client = new OrdersMicroserviceRestClient(restTemplate);

        client.urlOffers = "http://localhost:8080/offers";
        client.urlOrders  = "http://localhost:8080/orders";
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
                eq("http://localhost:8080/offers"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
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
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.getAllOrderPromotions(cart)
        );
        assertEquals("Error fetching applicable promotions", ex.getMessage());
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
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.getAllOrderPromotions(cart)
        );
        assertEquals("No promotions can be applied", ex.getMessage());
    }

    @Test
    void sentAOrder_success() {
        UUID expectedUuid = UUID.randomUUID();
        ResponseEntity<UUID> responseEntity =
                new ResponseEntity<>(expectedUuid, HttpStatus.CREATED);

        when(restTemplate.exchange(
                eq("http://localhost:8080/orders"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        OrderDTO dummyOrder = new OrderDTO();
        UUID actualUuid = client.sendAOrder(dummyOrder);

        assertEquals(expectedUuid, actualUuid);
    }

    @Test
    void sentAOrder_errorOnStatus() {
        ResponseEntity<UUID> badResponse =
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                eq("http://localhost:8080/orders"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(badResponse);

        OrderDTO dummyOrder = new OrderDTO();
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.sendAOrder(dummyOrder)
        );

        assertNotNull(ex.getMessage());
    }

    @Test
    void sentAOrder_nullBody() {
        ResponseEntity<UUID> responseNullBody =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/orders"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseNullBody);

        OrderDTO dummyOrder = new OrderDTO();
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.sendAOrder(dummyOrder)
        );

        assertNotNull(ex.getMessage());
    }
}
