package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionSeason;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class PromotionSeasonStrategy implements PromotionStrategy{

    @Override
    public boolean supports(Promotion promotion) {
        return promotion instanceof PromotionSeason;
    }


    @Override
    public BigDecimal apply(Promotion promotion, Cart cart, Map<Long, Product> productMap) {
        PromotionSeason seasonPromo = (PromotionSeason) promotion;
        BigDecimal total = BigDecimal.ZERO;

        for (CartDetail detail : cart.getCartDetails()) {
            Product product = productMap.get(detail.getProductId());
            BigDecimal unitPrice = product.getPrice();
            BigDecimal baseLineTotal = unitPrice.multiply(BigDecimal.valueOf(detail.getQuantity()));

            if (seasonPromo.getAffectedCategories().contains(product.getCategory())) {
                baseLineTotal = baseLineTotal.multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(seasonPromo.getDiscount())));
            }
            detail.setTotalPrice(baseLineTotal);
            total = total.add(baseLineTotal);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

}
