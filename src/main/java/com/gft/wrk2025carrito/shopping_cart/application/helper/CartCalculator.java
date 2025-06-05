package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;

import com.gft.wrk2025carrito.shopping_cart.application.service.client.OrderMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.application.service.client.ProductMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CartCalculator {

    private static ProductMicroserviceService productsMicroserviceService;
    private static OrderMicroserviceService ordersMicroserviceService;

    public CartCalculator(ProductMicroserviceService productsMicroserviceService, OrderMicroserviceService ordersMicroserviceService) {
        CartCalculator.productsMicroserviceService = productsMicroserviceService;
        CartCalculator.ordersMicroserviceService = ordersMicroserviceService;
    }

    private static final List<PromotionStrategy> strategies = List.of(
            new PromotionSeasonStrategy(),
            new PromotionQuantityStrategy()
    );

    public Cart calculateAndUpdateCart(Cart cart) throws Exception {
        Map<Long, Product> productMap = productsMicroserviceService.getProductsFromCart(cart);
        List<Promotion> promotions = productsMicroserviceService.getAllApplicablePromotions(cart);
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

        cart.setTotalWeight(totalWeight);
        cart.setTotalPrice(subtotal.add(calculateShippingCost(cart.getTotalWeight())));

        return cart;
    }

    public static BigDecimal applyTax(BigDecimal price, Double taxRate)throws Exception  {
        validatePercentageAndAmount(price, taxRate, "Tax");
        BigDecimal normalizedTax = BigDecimal.valueOf(taxRate);
        BigDecimal calculatedPrice = price.multiply(BigDecimal.ONE.add(normalizedTax));
        return round(calculatedPrice);
    }

    public static BigDecimal applyCharge(BigDecimal price, Double chargeRate)throws Exception  {
        validatePercentageAndAmount(price, chargeRate, "Charge");
        BigDecimal normalizedCharge = BigDecimal.valueOf(chargeRate);
        BigDecimal calculatedPrice = price.multiply(BigDecimal.ONE.add( normalizedCharge));
        return round(calculatedPrice);
    }

    private static BigDecimal calculateShippingCost(Double totalWeight) {
        if (totalWeight == null) {
            return BigDecimal.ZERO;
        }

        if (totalWeight <= 5.0) {
            return BigDecimal.valueOf(5);
        }
        if (totalWeight <= 10.0) {
            return BigDecimal.valueOf(10);
        }
        if (totalWeight <= 20.0) {
            return BigDecimal.valueOf(20);
        }

        return BigDecimal.valueOf(50);
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
        if (promotions == null || promotions.isEmpty()) {
            return calculateSubtotalFromCartDetails(cart);
        }

        for (Promotion promo : promotions) {
            for (PromotionStrategy strategy : strategies) {
                if (strategy.supports(promo)) {
                    strategy.apply(promo, cart, productMap);
                    break;
                }
            }
        }
        return calculateSubtotalFromCartDetails(cart);
    }

}
