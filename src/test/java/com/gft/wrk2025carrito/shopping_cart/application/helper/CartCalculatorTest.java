package com.gft.wrk2025carrito.shopping_cart.application.helper;

import com.gft.wrk2025carrito.shopping_cart.application.dto.Product;
import com.gft.wrk2025carrito.shopping_cart.application.dto.Promotion;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionQuantity;
import com.gft.wrk2025carrito.shopping_cart.application.dto.PromotionSeason;
import com.gft.wrk2025carrito.shopping_cart.application.service.client.ProductMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartCalculatorTest {

    @Mock
    private ProductMicroserviceService productService;

    private Cart cart;
    private Product product;
    private PromotionQuantity promoQuantity;
    private PromotionSeason promoSeason;

    @InjectMocks
    private CartCalculator cartCalculator;

    @BeforeEach
    void setUp() {

        product = new Product(1L, "Zapato", "Calzado", BigDecimal.TEN, 1.5);

        // Quantity promo
        promoQuantity = new PromotionQuantity(3, "Calzado");
        promoQuantity.setId(100L);
        promoQuantity.setPromotionType("QUANTITY");
        promoQuantity.setDiscount(0.2);

        promoSeason = new PromotionSeason("REBAJAS", List.of("Calzado"));
        promoSeason.setId(200L);
        promoSeason.setPromotionType("SEASON");
        promoSeason.setDiscount(0.1);

        CartDetail detail = new CartDetail();
        detail.setProductId(1L);
        detail.setQuantity(2);

        cart = new Cart();
        cart.setId(new CartId());
        cart.setCartDetails(List.of(detail));
        cart.setPromotionIds(List.of(100L, 200L)); // ambas promos

        lenient().when(productService.getProductsFromCart(cart)).thenReturn(Map.of(1L, product));
        lenient().when(productService.getAllApplicablePromotions(cart)).thenReturn(List.of(promoQuantity, promoSeason));
    }

    @Test
    void testCalculateAndUpdateCart_ACTIVE() throws Exception {
        cart.setState(CartState.ACTIVE);

        Cart result = cartCalculator.calculateAndUpdateCart(cart);

        assertNotNull(result);
        assertEquals(new BigDecimal("25.00"), result.getTotalPrice());
        assertEquals(3.0, result.getTotalWeight());
    }

    @Test
    void testCalculateAndUpdateCart_PENDING() throws Exception {
        cart.setState(CartState.PENDING);

        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.1); // 10%
        cart.setPaymentMethod(method);

        CountryTax tax = new CountryTax();
        tax.setTax(0.2); // 20%
        cart.setCountryTax(tax);

        Cart result = cartCalculator.calculateAndUpdateCart(cart);

        assertNotNull(result);
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCalculateAndUpdateCart_CLOSED() throws Exception {
        cart.setState(CartState.CLOSED);

        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.05); // 5%
        cart.setPaymentMethod(method);

        CountryTax tax = new CountryTax();
        tax.setTax(0.21); // 21%
        cart.setCountryTax(tax);

        Cart result = cartCalculator.calculateAndUpdateCart(cart);

        assertNotNull(result);
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }


    @Test
    void testCalculateAndUpdateCart_ABANDONED() {
        cart.setState(CartState.ABANDONED);

        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.05); // 5%
        cart.setPaymentMethod(method);

        CountryTax tax = new CountryTax();
        tax.setTax(0.21); // 21%
        cart.setCountryTax(tax);

        Exception ex =  assertThrows(IllegalStateException.class, () -> cartCalculator.calculateAndUpdateCart(cart));
        assertEquals("Unsupported cart state: " + cart.getState(), ex.getMessage());
    }

    @Test
    void testCalculateAndUpdateCart_PENDING_with_noPaymentMethod() throws Exception {
        cart.setState(CartState.PENDING);

        cart.setPaymentMethod(null);

        cart.setCountryTax(null);

        Cart result = cartCalculator.calculateAndUpdateCart(cart);

        assertNotNull(result);
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCalculateAndUpdateCart_CLOSED_with_noPromotion() throws Exception {
        cart.setState(CartState.CLOSED);

        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.05); // 5%
        cart.setPaymentMethod(method);

        CountryTax tax = new CountryTax();
        tax.setTax(0.21); // 21%
        cart.setCountryTax(tax);

        when(productService.getAllApplicablePromotions(cart)).thenReturn(null);

        Cart result = cartCalculator.calculateAndUpdateCart(cart);

        assertNotNull(result);
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }
    @Test
    void testCalculateAndUpdateCart_CLOSED_with_emptyPromotion() throws Exception {
        cart.setState(CartState.CLOSED);

        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.05); // 5%
        cart.setPaymentMethod(method);

        CountryTax tax = new CountryTax();
        tax.setTax(0.21); // 21%
        cart.setCountryTax(tax);

        when(productService.getAllApplicablePromotions(cart)).thenReturn(List.of());

        Cart result = cartCalculator.calculateAndUpdateCart(cart);

        assertNotNull(result);
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCalculateAndUpdateCart_CLOSED_withNo_PaymentMethod() {
        cart.setState(CartState.CLOSED);

        cart.setPaymentMethod(null);

        CountryTax tax = new CountryTax();
        tax.setTax(0.21);
        cart.setCountryTax(tax);

        Exception ex =  assertThrows(IllegalStateException.class, () -> cartCalculator.calculateAndUpdateCart(cart));
        assertEquals("Closed cart must have payment method and country tax", ex.getMessage());
    }

    @Test
    void testCalculateAndUpdateCart_CLOSED_withNo_CountryTax() throws Exception {
        cart.setState(CartState.CLOSED);

        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.05);
        cart.setPaymentMethod(method);

        cart.setCountryTax(null);

        Exception ex =  assertThrows(IllegalStateException.class, () -> cartCalculator.calculateAndUpdateCart(cart));
        assertEquals("Closed cart must have payment method and country tax", ex.getMessage());
    }


    @Test
    void testCalculateAndUpdateCart_CLOSED_different_Promotion() throws Exception {
        cart.setState(CartState.CLOSED);

        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.05); // 5%
        cart.setPaymentMethod(method);

        CountryTax tax = new CountryTax();
        tax.setTax(0.21); // 21%
        cart.setCountryTax(tax);

        when(productService.getAllApplicablePromotions(cart)).thenReturn(List.of(new Promotion(){}));

        Cart result = cartCalculator.calculateAndUpdateCart(cart);
        assertNotNull(result);
        assertTrue(result.getTotalPrice().compareTo(BigDecimal.ZERO) > 0);
    }



    @Nested
    @DisplayName("applyTax tests")
    class ApplyTaxTests {

        @ParameterizedTest(name = "Price: {0}, Tax: {1} → Expected: {2}")
        @CsvSource({
                "100.00, 0.21, 121.00",
                "50.00, 0.00, 50.00"
        })
        void testApplyTax_ValidInputs(String priceStr, double taxRate, String expectedStr) throws Exception {
            BigDecimal price = new BigDecimal(priceStr);
            BigDecimal expected = new BigDecimal(expectedStr);

            BigDecimal result = CartCalculator.applyTax(price, taxRate);
            assertEquals(expected, result);
        }

        @Test
        void testApplyTax_NullRate_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(new BigDecimal("100"), null)
            );
            assertEquals("Tax rate cannot be null", ex.getMessage());
        }

        @Test
        void testApplyTax_NullPrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(null, 0.1)
            );
            assertEquals("Amount cannot be null", ex.getMessage());
        }

        @Test
        void testApplyTax_NegativePrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(new BigDecimal("-1"), 0.1)
            );
            assertEquals("Amount cannot be negative", ex.getMessage());
        }

        @ParameterizedTest
        @CsvSource({
                "1.1", "-0.1"
        })
        void testApplyTax_InvalidRate_shouldThrow(double invalidRate) {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyTax(new BigDecimal("10"), invalidRate)
            );
            assertEquals("Tax rate must be between 0 and 1", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("applyCharge tests")
    class ApplyChargeTests {

        @ParameterizedTest(name = "Price: {0}, Charge: {1} → Expected: {2}")
        @CsvSource({
                "100.00, 0.10, 110.00",
                "50.00, 0.00, 50.00"
        })
        void testApplyCharge_ValidInputs(String priceStr, double chargeRate, String expectedStr) throws Exception {
            BigDecimal price = new BigDecimal(priceStr);
            BigDecimal expected = new BigDecimal(expectedStr);

            BigDecimal result = CartCalculator.applyCharge(price, chargeRate);
            assertEquals(expected, result);

        }

        @Test
        void testApplyCharge_NullRate_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(new BigDecimal("100"), null)
            );
            assertEquals("Charge rate cannot be null", ex.getMessage());
        }

        @Test
        void testApplyCharge_NullPrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(null, 0.1)
            );
            assertEquals("Amount cannot be null", ex.getMessage());
        }

        @Test
        void testApplyCharge_NegativePrice_shouldThrow() {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(new BigDecimal("-1"), 0.1)
            );
            assertEquals("Amount cannot be negative", ex.getMessage());
        }

        @ParameterizedTest
        @CsvSource({
                "1.5", "-0.1"
        })
        void testApplyCharge_InvalidRate_shouldThrow(double invalidRate) {
            Exception ex = assertThrows(Exception.class, () ->
                    CartCalculator.applyCharge(new BigDecimal("10"), invalidRate)
            );
            assertEquals("Charge rate must be between 0 and 1", ex.getMessage());
        }
    }
}