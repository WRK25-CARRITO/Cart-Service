package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionSeason;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PromotionSeasonStrategyTest {

    @Test
    public void testApply_WithMatchingCategory_ShouldApplyDiscount() {
        // Arrange
        PromotionSeasonStrategy strategy = new PromotionSeasonStrategy();
        PromotionSeason promo = new PromotionSeason();
        promo.setDiscount(0.2); // 20%
        promo.setAffectedCategories(List.of("Electronics"));

        Product product = new Product();
        product.setId(1L);
        product.setCategory("Electronics");
        product.setPrice(new BigDecimal("100.00"));

        CartDetail detail = mock(CartDetail.class);
        when(detail.getProductId()).thenReturn(1L);
        when(detail.getQuantity()).thenReturn(2);

        Cart cart = mock(Cart.class);
        when(cart.getCartDetails()).thenReturn(List.of(detail));

        Map<Long, Product> productMap = Map.of(1L, product);

        BigDecimal total = strategy.apply(promo, cart, productMap);


        assertEquals(new BigDecimal("160.00"), total);
    }

    @Test
    public void testApply_WithNonMatchingCategory_ShouldNotApplyDiscount() {
        // Arrange
        PromotionSeasonStrategy strategy = new PromotionSeasonStrategy();
        PromotionSeason promo = new PromotionSeason();
        promo.setDiscount(0.2);
        promo.setAffectedCategories(List.of("Toys"));

        Product product = new Product();
        product.setId(2L);
        product.setCategory("Books");
        product.setPrice(new BigDecimal("50.00"));

        CartDetail detail = mock(CartDetail.class);
        when(detail.getProductId()).thenReturn(2L);
        when(detail.getQuantity()).thenReturn(1);

        Cart cart = mock(Cart.class);
        when(cart.getCartDetails()).thenReturn(List.of(detail));

        Map<Long, Product> productMap = Map.of(2L, product);

        // Act
        BigDecimal total = strategy.apply(promo, cart, productMap);

        // Assert
        assertEquals(new BigDecimal("50.00"), total);
    }

    @Test
    public void testSupports_ShouldReturnTrueForPromotionSeason() {
        PromotionSeasonStrategy strategy = new PromotionSeasonStrategy();
        PromotionSeason promo = new PromotionSeason();
        assertTrue(strategy.supports(promo));
    }

    @Test
    public void testSupports_ShouldReturnFalseForOtherPromotionType() {
        PromotionSeasonStrategy strategy = new PromotionSeasonStrategy();
        Promotion mockOtherPromo = mock(Promotion.class);
        assertFalse(strategy.supports(mockOtherPromo)); // as it's not PromotionSeason
    }

}