package com.gft.wrk2025carrito.shopping_cart.infrastructure.client;

import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.service.client.OrderMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component

public class OrdersMicroserviceRestClient implements OrderMicroserviceService {

    private final RestTemplate restTemplate;
    @Value("${app.ordersOffersBackendURL}")
    String urlOffers;

    @Value("${app.ordersBackendURL}")
    String url;

    public OrdersMicroserviceRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UUID sendAOrder(OrderDTO order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(order, headers);

        ResponseEntity<UUID> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        HttpStatusCode status = response.getStatusCode();
        if (status != HttpStatus.CREATED) {
            throw new IllegalStateException(
                    "Error sending a order " + status
            );
        }
        return response.getBody();
    }

    @Override
    public List<Long> getAllOrderPromotions(Map<Long,Integer> cartDetailProducts) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<Long,Integer>> request = new HttpEntity<>(cartDetailProducts, headers);

        ResponseEntity<List<Long>> response = restTemplate.exchange(
                urlOffers,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("Error fetching applicable promotions");
        }

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new IllegalStateException("No promotions can be applied"))
                .stream()
                .toList();
    }

}
