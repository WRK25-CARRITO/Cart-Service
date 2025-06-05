package com.gft.wrk2025carrito.shopping_cart.infrastructure.client;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.service.client.ProductMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductsMicroserviceRestClient implements ProductMicroserviceService {

    private final RestTemplate restTemplate;

    @Value("${app.productsBackendURL}")
    String baseUrl;

    public ProductsMicroserviceRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<Long,Product> getProductsFromCart(Cart cart) {
        List<Long> productIds = cart.getCartDetails().stream()
                .map(CartDetail::getProductId)
                .toList();

        String url = baseUrl + "/products/list-by-ids";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> request = new HttpEntity<>(productIds, headers);

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
        );

        if(response.getStatusCode()!= HttpStatus.OK)
            throw new IllegalStateException("Error fetching products");

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new IllegalStateException("No products found in response"))
                .stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
    }

    @Override
    public List<Promotion> getAllApplicablePromotions(Cart cart) {
        if (cart.getPromotionIds().isEmpty()) {
                return Collections.emptyList();
        }

        String url = baseUrl + "/promotions";

        ResponseEntity<List<Promotion>> promoResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
        );

        return Optional.ofNullable(promoResponse.getBody())
                    .orElseThrow(() -> new IllegalStateException("No promotions found in response"))
                    .stream()
                    .filter(p -> cart.getPromotionIds().contains(p.getId()))
                    .toList();
//        return promoResponse.getBody();
        }
    }

