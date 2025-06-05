package com.gft.wrk2025carrito.shopping_cart.infrastructure.client;

import com.gft.wrk2025carrito.shopping_cart.application.service.client.OrderMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OrdersMicroserviceRestClient implements OrderMicroserviceService {

    private final RestTemplate restTemplate;
    @Value("${app.ordersBackendURL}")
    String url;

    public OrdersMicroserviceRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Long> getAllOrderPromotions(Cart cart) {

        Map<String, Integer> cartDetailProducts = new HashMap<>();
        for (CartDetail detail : cart.getCartDetails()) {
            cartDetailProducts.put(detail.getProductId().toString(), detail.getQuantity());
        }

        if (cartDetailProducts.isEmpty()) {
            throw new IllegalArgumentException("Cart with id " + cart.getId() + " is empty");
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        cartDetailProducts.forEach((stringId, quantity) -> {
            builder.queryParam(stringId, quantity);
        });
        String uri = builder.toUriString();

        ResponseEntity<List<Long>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("Error fetching applicable promotions: " + response.getStatusCode());
        }

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new IllegalStateException("No promotions can be applied"))
                .stream()
                .toList();
    }

}
