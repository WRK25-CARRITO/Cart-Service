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

        client.url = "http://localhost:8080/offers";
    }

    @Test
    void getAllOrderPromotions_success() {
        Map<Long, Integer> cartDetailProducts = Map.of(
                100L, 2,
                200L, 5
        );

        List<Long> fakePromos = List.of(10L, 20L, 30L);
        ResponseEntity<List<Long>> response =
                new ResponseEntity<>(fakePromos, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/offers"),
                eq(HttpMethod.POST),
                argThat((HttpEntity<Map<Long, Integer>> requestEntity) -> {
                    return requestEntity.getBody().equals(cartDetailProducts)
                            && requestEntity.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON);
                }),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<Long> result = client.getAllOrderPromotions(cartDetailProducts);

        assertNotNull(result);
        assertEquals(3, result.size());assertEquals(List.of(10L, 20L, 30L), result);
    }

    @Test
    void getAllOrderPromotions_errorOnStatus() {
        Map<Long, Integer> cartDetailProducts = Map.of(
                42L, 1
        );

        ResponseEntity<List<Long>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.exchange(
                eq("http://localhost:8080/offers"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.getAllOrderPromotions(cartDetailProducts)
        );
        assertEquals("Error fetching applicable promotions", ex.getMessage());
    }

    @Test
    void getAllOrderPromotions_nullBody() {
        Map<Long, Integer> cartDetailProducts = Map.of(
                7L, 3
        );

        ResponseEntity<List<Long>> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/offers"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> client.getAllOrderPromotions(cartDetailProducts)
        );
        assertEquals("No promotions can be applied", ex.getMessage());
    }

    @Test
    void getAllOrderPromotions_emptyListBody() {
        Map<Long, Integer> cartDetailProducts = Map.of(
                9L, 4
        );

        ResponseEntity<List<Long>> response = new ResponseEntity<>(List.of(), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/offers"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<Long> result = client.getAllOrderPromotions(cartDetailProducts);
        assertNotNull(result);
        assertTrue(result.isEmpty());
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
