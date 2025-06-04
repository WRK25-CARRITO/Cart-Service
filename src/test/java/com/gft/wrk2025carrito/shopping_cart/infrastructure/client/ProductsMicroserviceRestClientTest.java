package com.gft.wrk2025carrito.shopping_cart.infrastructure.client;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionQuantity;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductsMicroserviceRestClientTest {

    private RestTemplate restTemplate;
    private ProductsMicroserviceRestClient client;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        client = new ProductsMicroserviceRestClient(restTemplate);
        client.baseUrl = "http://localhost:8080";
    }

    @Test
    void getProductsFromCart_success() {
        Cart cart = createCartWithProduct();
        Product product = new Product(1L, "Zapato", "Calzado cómodo", BigDecimal.TEN, 1.5);

        ResponseEntity<List<Product>> response = new ResponseEntity<>(List.of(product), HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        Map<Long, Product> result = client.getProductsFromCart(cart);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Zapato", result.get(1L).getName());
    }

    @Test
    void getProductsFromCart_errorOnStatus() {
        Cart cart = createCartWithProduct();

        ResponseEntity<List<Product>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        Exception ex = assertThrows(IllegalStateException.class, () -> client.getProductsFromCart(cart));
        assertEquals("Error fetching products", ex.getMessage());
    }

    @Test
    void getProductsFromCart_nullBody() {
        Cart cart = createCartWithProduct();

        ResponseEntity<List<Product>> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        Exception ex = assertThrows(IllegalStateException.class, () -> client.getProductsFromCart(cart));
        assertEquals("No products found in response", ex.getMessage());
    }

    @Test
    void getAllApplicablePromotions_success() {
        Cart cart = new Cart();
        cart.setPromotionIds(List.of(10L));

        Promotion promotion = new PromotionQuantity(3, "Calzado");
        promotion.setId(10L);
        promotion.setPromotionType("QUANTITY");

        ResponseEntity<List<Promotion>> response = new ResponseEntity<>(List.of(promotion), HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        List<Promotion> result = client.getAllApplicablePromotions(cart);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
    }

    @Test
    void getAllApplicablePromotions_emptyListWhenNoIds() {
        Cart cart = new Cart();
        cart.setPromotionIds(Collections.emptyList());

        List<Promotion> result = client.getAllApplicablePromotions(cart);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllApplicablePromotions_nullBody() {
        Cart cart = new Cart();
        cart.setPromotionIds(List.of(10L));

        ResponseEntity<List<Promotion>> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        Exception ex = assertThrows(IllegalStateException.class, () -> client.getAllApplicablePromotions(cart));
        assertEquals("No promotions found in response", ex.getMessage());
    }

    private Cart createCartWithProduct() {
        CartDetail detail = new CartDetail();
        detail.setProductId(1L);
        detail.setQuantity(2);

        Cart cart = new Cart();
        cart.setCartDetails(List.of(detail));
        return cart;
    }
}
