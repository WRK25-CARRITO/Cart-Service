package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionQuantity;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class PromotionQuantityStrategy implements PromotionStrategy {

    @Override
    public boolean supports(Promotion promotion) {
        return promotion instanceof PromotionQuantity;
    }

    @Override
    public BigDecimal apply(Promotion promotion, Cart cart, Map<Long, Product> productMap) {
        PromotionQuantity quantityPromo = (PromotionQuantity) promotion;
        BigDecimal total = BigDecimal.ZERO;

        int totalCategoryQuantity = cart.getCartDetails().stream()
                .filter(d -> productMap.get(d.getProductId()).getCategory().equals(quantityPromo.getCategory()))
                .mapToInt(CartDetail::getQuantity)
                .sum();

        for (
                CartDetail detail : cart.getCartDetails()) {
            Product product = productMap.get(detail.getProductId());
            BigDecimal unitPrice = product.getPrice();
            BigDecimal baseLineTotal = unitPrice.multiply(BigDecimal.valueOf(detail.getQuantity()));

            if (product.getCategory().equals(quantityPromo.getCategory()) && totalCategoryQuantity >= quantityPromo.getQuantity()) {
                baseLineTotal = baseLineTotal.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(quantityPromo.getDiscount())));
            }
            detail.setTotalPrice(baseLineTotal);
            total = total.add(baseLineTotal);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

}