package com.gft.wrk2025carrito.shopping_cart.infrastructure.client;

import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
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
    String urlOrders;

    public OrdersMicroserviceRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Long> getAllOrderPromotions(Cart cart) {

        List<Long> productIds = cart.getCartDetails().stream()
                .map(CartDetail::getProductId)
                .toList();

        List<Integer> productQuantities = cart.getCartDetails().stream()
                .map(CartDetail::getQuantity)
                .toList();

        Map<Long,Integer> cartDetailProducts = new HashMap<>();

        int mapSize = Math.min(productIds.size(), productQuantities.size());
        for (int i = 0; i < mapSize; i++) {
            cartDetailProducts.put(productIds.get(i), productQuantities.get(i));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<Long,Integer>> request = new HttpEntity<>(cartDetailProducts, headers);

        ResponseEntity<List<Long>> response = restTemplate.exchange(
                urlOffers,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        if(response.getStatusCode()!= HttpStatus.OK)
            throw new IllegalStateException("Error fetching applicable promotions");

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new IllegalStateException("No promotions can be applied"))
                .stream()
                .toList();
    }

    @Override
    public UUID sendAOrder(OrderDTO order) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(order, headers);

        ResponseEntity<UUID> response = restTemplate.exchange(
                urlOrders,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<UUID>() {}
        );

        HttpStatusCode status = response.getStatusCode();
        if (status != HttpStatus.CREATED) {
            throw new IllegalStateException(
                    "Error sending a order " + status
            );
        }

        return response.getBody();
    }

}
