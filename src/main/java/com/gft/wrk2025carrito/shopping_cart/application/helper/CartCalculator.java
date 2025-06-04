package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionQuantity;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionSeason;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartCalculator {

    private static final List<PromotionStrategy> strategies = List.of(
            new PromotionSeasonStrategy(),
            new PromotionQuantityStrategy()
    );

    public Cart calculateAndUpdateCart(Cart cart, RestTemplate restTemplate) throws Exception {
        Map<Long, Product> productMap = fetchProducts(cart, restTemplate);
        List<Promotion> promotions = fetchApplicablePromotions(restTemplate,cart );
        updateCartDetailsFromProducts(cart, productMap);

        BigDecimal subtotal = switch (cart.getState()) {
            case ACTIVE -> calculateSubtotalFromCartDetails(cart);
            case PENDING -> applyOptionalChargesAndTaxes(applyPromotions(cart, promotions, productMap), cart);
            case CLOSED -> applyRequiredChargesAndTaxes(applyPromotions(cart, promotions, productMap), cart);
            default -> throw new IllegalStateException("Unsupported cart state: " + cart.getState());
        };

        Double totalWeight = cart.getCartDetails().stream()
                .map(CartDetail::getTotalWeight)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        cart.setTotalPrice(subtotal);
        cart.setTotalWeight(totalWeight);
        return cart;
    }

    private static BigDecimal applyTax(BigDecimal price, Double taxRate)throws Exception  {
        validatePercentageAndAmount(price, taxRate, "Tax");
        BigDecimal normalizedTax = BigDecimal.valueOf(taxRate);
        BigDecimal calculatedPrice = price.multiply(BigDecimal.ONE.add(normalizedTax));
        return round(calculatedPrice);
    }

    private static BigDecimal applyCharge(BigDecimal price, Double chargeRate)throws Exception  {
        validatePercentageAndAmount(price, chargeRate, "Charge");
        BigDecimal normalizedCharge = BigDecimal.valueOf(chargeRate);
        BigDecimal calculatedPrice = price.multiply(BigDecimal.ONE.add( normalizedCharge));
        return round(calculatedPrice);
    }

    private static void validatePercentageAndAmount(BigDecimal amount, Double rate, String context) throws Exception {
        if (rate == null) {
            throw new Exception(context + " rate cannot be null");
        }
        if (amount == null) {
            throw new Exception("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Amount cannot be negative");
        }
        if (rate < 0 || rate > 1) {
            throw new Exception(context + " rate must be between 0 and 1");
        }
    }

    private static BigDecimal round(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private static Map<Long, Product> fetchProducts(Cart cart, RestTemplate restTemplate) {
        List<Long> productIds = cart.getCartDetails().stream()
                .map(CartDetail::getProductId)
                .toList();

        String url = "https://workshop-7uvd.onrender.com/api/v1/products/list-by-ids";

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

    private static List<Promotion> fetchApplicablePromotions(RestTemplate restTemplate, Cart cart) {
        if (cart.getPromotionIds() == null || cart.getPromotionIds().isEmpty()) {
            return Collections.emptyList();
        }

        ResponseEntity<List<Promotion>> promoResponse = restTemplate.exchange(
                "https://workshop-7uvd.onrender.com/api/v1/promotions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return Optional.ofNullable(promoResponse.getBody())
                .orElseThrow(() -> new IllegalStateException("No promotions found in response"))
                .stream()
                .filter(p -> cart.getPromotionIds().contains(p.getId()))
                .toList();
    }

    private static BigDecimal applyOptionalChargesAndTaxes(BigDecimal subtotal, Cart cart) throws Exception {
        BigDecimal withCharge = cart.getPaymentMethod() != null
                ? applyCharge(subtotal, cart.getPaymentMethod().getCharge())
                : subtotal;

        return cart.getCountryTax() != null
                ? applyTax(withCharge, cart.getCountryTax().getTax())
                : withCharge;
    }

    private static BigDecimal applyRequiredChargesAndTaxes(BigDecimal subtotal, Cart cart) throws Exception {
        if (cart.getPaymentMethod() == null || cart.getCountryTax() == null) {
            throw new IllegalStateException("Closed cart must have payment method and country tax");
        }
        BigDecimal withCharge = applyCharge(subtotal, cart.getPaymentMethod().getCharge());
        return applyTax(withCharge, cart.getCountryTax().getTax());
    }

    private static void updateCartDetailsFromProducts(Cart cart, Map<Long, Product> productMap) {
        for (CartDetail detail : cart.getCartDetails()) {
            Product product = productMap.get(detail.getProductId());
            BigDecimal basePrice = product.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            Double baseWeight = product.getWeight() * detail.getQuantity();

            detail.setTotalPrice(basePrice.setScale(2, RoundingMode.HALF_UP));
            detail.setTotalWeight(baseWeight);
        }
    }

    private static BigDecimal calculateSubtotalFromCartDetails(Cart cart) {
        return cart.getCartDetails().stream()
                .map(CartDetail::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal applyPromotions(Cart cart, List<Promotion> promotions, Map<Long, Product> productMap) {
        if (promotions.isEmpty()) {
            return calculateSubtotalFromCartDetails(cart);
        }

        for (Promotion promo : promotions) {
            if (promo instanceof PromotionQuantity) {
                for (PromotionStrategy strategy : strategies) {
                    if (strategy.supports(promo)) {
                        strategy.apply(promo, cart, productMap);
                        break;
                    }
                }
            }
        }

        for (Promotion promo : promotions) {
            if (promo instanceof PromotionSeason) {
                for (PromotionStrategy strategy : strategies) {
                    if (strategy.supports(promo)) {
                        strategy.apply(promo, cart, productMap);
                        break;
                    }
                }
            }
        }

        return calculateSubtotalFromCartDetails(cart);
    }

}