package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionQuantity;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PromotionQuantityStrategyTest {

    @Test
    public void testApply_WithEnoughQuantity_ShouldApplyDiscount() {
        // Arrange
        PromotionQuantityStrategy strategy = new PromotionQuantityStrategy();
        PromotionQuantity promo = new PromotionQuantity();
        promo.setCategory("Books");
        promo.setQuantity(3); // mínimo 3 unidades
        promo.setDiscount(0.1); // 10%

        Product product = new Product();
        product.setId(1L);
        product.setCategory("Books");
        product.setPrice(new BigDecimal("50.00"));

        CartDetail detail1 = mock(CartDetail.class);
        when(detail1.getProductId()).thenReturn(1L);
        when(detail1.getQuantity()).thenReturn(2); // 2 unidades

        CartDetail detail2 = mock(CartDetail.class);
        when(detail2.getProductId()).thenReturn(1L);
        when(detail2.getQuantity()).thenReturn(2); // 2 más = 4 total

        Cart cart = mock(Cart.class);
        when(cart.getCartDetails()).thenReturn(List.of(detail1, detail2));

        Map<Long, Product> productMap = Map.of(1L, product);

        // Act
        BigDecimal total = strategy.apply(promo, cart, productMap);

        // Assert: (4 * 50.00) = 200.00 -> with 10% discount = 180.00
        assertEquals(new BigDecimal("180.00"), total);
    }

    @Test
    public void testApply_WithInsufficientQuantity_ShouldNotApplyDiscount() {
        // Arrange
        PromotionQuantityStrategy strategy = new PromotionQuantityStrategy();
        PromotionQuantity promo = new PromotionQuantity();
        promo.setCategory("Books");
        promo.setQuantity(5); // mínimo 5 unidades
        promo.setDiscount(0.2); // 20%

        Product product = new Product();
        product.setId(2L);
        product.setCategory("Books");
        product.setPrice(new BigDecimal("40.00"));

        CartDetail detail = mock(CartDetail.class);
        when(detail.getProductId()).thenReturn(2L);
        when(detail.getQuantity()).thenReturn(3); // solo 3

        Cart cart = mock(Cart.class);
        when(cart.getCartDetails()).thenReturn(List.of(detail));

        Map<Long, Product> productMap = Map.of(2L, product);

        // Act
        BigDecimal total = strategy.apply(promo, cart, productMap);

        // Assert: no discount -> 3 * 40.00 = 120.00
        assertEquals(new BigDecimal("120.00"), total);
    }

    @Test
    public void testApply_WithMixedCategories_ShouldOnlyDiscountMatchingCategory() {
        // Arrange
        PromotionQuantityStrategy strategy = new PromotionQuantityStrategy();
        PromotionQuantity promo = new PromotionQuantity();
        promo.setCategory("Toys");
        promo.setQuantity(2); // mínimo 2
        promo.setDiscount(0.5); // 50%

        Product toysProduct = new Product();
        toysProduct.setId(1L);
        toysProduct.setCategory("Toys");
        toysProduct.setPrice(new BigDecimal("30.00"));

        Product bookProduct = new Product();
        bookProduct.setId(2L);
        bookProduct.setCategory("Books");
        bookProduct.setPrice(new BigDecimal("20.00"));

        CartDetail toyDetail = mock(CartDetail.class);
        when(toyDetail.getProductId()).thenReturn(1L);
        when(toyDetail.getQuantity()).thenReturn(2); // suficiente

        CartDetail bookDetail = mock(CartDetail.class);
        when(bookDetail.getProductId()).thenReturn(2L);
        when(bookDetail.getQuantity()).thenReturn(1);

        Cart cart = mock(Cart.class);
        when(cart.getCartDetails()).thenReturn(List.of(toyDetail, bookDetail));

        Map<Long, Product> productMap = Map.of(
                1L, toysProduct,
                2L, bookProduct
        );

        // Act
        BigDecimal total = strategy.apply(promo, cart, productMap);

        // Assert: Toys -> 2 * 30 = 60 -> 50% = 30, Books -> 1 * 20 = 20 -> total = 50
        assertEquals(new BigDecimal("50.00"), total);
    }

    @Test
    public void testSupports_ShouldReturnTrueForPromotionQuantity() {
        PromotionQuantityStrategy strategy = new PromotionQuantityStrategy();
        PromotionQuantity promo = new PromotionQuantity();
        assertTrue(strategy.supports(promo));
    }

    @Test
    public void testSupports_ShouldReturnFalseForOtherPromotion() {
        PromotionQuantityStrategy strategy = new PromotionQuantityStrategy();
        Promotion mockOtherPromo = mock(Promotion.class);
        assertFalse(strategy.supports(mockOtherPromo));
    }
}
