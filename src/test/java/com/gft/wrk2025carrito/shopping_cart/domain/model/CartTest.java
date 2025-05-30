package com.gft.wrk2025carrito.shopping_cart.domain.model;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethodId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private UUID userId;
    private CartId cartId;
    private List<CartDetail> cartDetails;
    private CountryTax countryTax;
    private PaymentMethod paymentMethod;
    private Date now;
    private List<Long> promotionsId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        cartId = new CartId();
        cartDetails = new ArrayList<>();
        countryTax = CountryTax.build(new CountryTaxId(), "Spain", 0.3);
        paymentMethod = PaymentMethod.build(new PaymentMethodId(), "Test", 0.5);
        now = new Date();
        promotionsId = new ArrayList<>();
    }

    @Test
    void create_cart_ok() {
        Cart cart = Cart.build(
                cartId,
                userId,
                null,
                null,
                BigDecimal.valueOf(10.0),
                100.0,
                now,
                now,
                cartDetails,
                CartState.ACTIVE,
                new ArrayList<>()
        );

        assertNotNull(cart.getId());
        assertNotNull(cart.getCartDetails());
        assertEquals(userId, cart.getUserId());
        assertEquals(100.0, cart.getTotalWeight());
        assertEquals(BigDecimal.valueOf(10.0), cart.getTotalPrice());
    }

    @Test
    void create_cart_fail_cartId_null() {
        assertThrows(IllegalArgumentException.class, () ->
                Cart.build(
                        null,
                        userId,
                        countryTax,
                        paymentMethod,
                        BigDecimal.TEN,
                        100.0,
                        now,
                        now,
                        cartDetails,
                        CartState.ACTIVE,
                        new ArrayList<>()
                ));
    }

    @Test
    void create_cart_fail_negative_weight() {
        assertThrows(IllegalArgumentException.class, () ->
                Cart.build(
                        cartId,
                        userId,
                        countryTax,
                        paymentMethod,
                        BigDecimal.TEN,
                        -5.0,
                        now,
                        now,
                        cartDetails,
                        CartState.ACTIVE,
                        new ArrayList<>()
                ));
    }

    @Test
    void create_cart_fail_negative_price() {
        assertThrows(IllegalArgumentException.class, () ->
                Cart.build(
                        cartId,
                        userId,
                        countryTax,
                        paymentMethod,
                        BigDecimal.valueOf(-10.0),
                        100.0,
                        now,
                        now,
                        cartDetails,
                        CartState.ACTIVE,
                        new ArrayList<>()
                ));
    }

    @Test
    void create_cart_fail_cartDetails_null() {
        Cart cart =  Cart.build(
                cartId,
                userId,
                countryTax,
                paymentMethod,
                BigDecimal.TEN,
                100.0,
                now,
                now,
                null,
                CartState.CLOSED,
                null
        );

        assertEquals(Collections.emptyList(), cart.getCartDetails());
    }

    @Test
    void create_cart_success_pending_with_tax_and_payment() {
        Cart cart = Cart.build(
                cartId,
                userId,
                countryTax,
                paymentMethod,
                BigDecimal.TEN,
                100.0,
                now,
                now,
                cartDetails,
                CartState.PENDING,
                new ArrayList<>()
        );
        assertNotNull(cart);
    }

    @Test
    void create_cart_success_pending_without_tax_and_payment() {
        Cart cart = Cart.build(
                cartId,
                userId,
                null,
                null,
                BigDecimal.TEN,
                100.0,
                now,
                now,
                cartDetails,
                CartState.PENDING,
                new ArrayList<>()
        );
        assertNotNull(cart);
    }

    @Test
    void create_cart_fail_non_pending_or_closed_with_tax_and_payment() {
        assertThrows(IllegalArgumentException.class, () ->
                Cart.build(
                        cartId,
                        userId,
                        countryTax,
                        paymentMethod,
                        BigDecimal.TEN,
                        100.0,
                        now,
                        now,
                        cartDetails,
                        CartState.ACTIVE,
                        new ArrayList<>()
                ));
    }

    @Test
    void create_cart_fail_null_state() {
        assertThrows(IllegalArgumentException.class, () ->
                Cart.build(
                        cartId,
                        userId,
                        countryTax,
                        paymentMethod,
                        BigDecimal.TEN,
                        100.0,
                        now,
                        now,
                        cartDetails,
                        null,
                        new ArrayList<>()
                ));
    }

    @Test
    void create_cart_fail_onCreatedAt_isNull() {
        assertThrows(IllegalArgumentException.class, () ->
                Cart.build(
                        cartId,
                        userId,
                        null,
                        null,
                        BigDecimal.TEN,
                        100.0,
                        null,
                        now,
                        cartDetails,
                        CartState.ACTIVE,
                        new ArrayList<>()
                ));
    }

    @Test
    void create_cart_fail_paymentMethod_not_null_when_state_not_pending_or_closed() {
        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, null, paymentMethod, BigDecimal.valueOf(100.0), 5.0, now, now, cartDetails, CartState.ACTIVE, promotionsId);
        });
    }


    @Test
    void create_cart_success_closed_with_tax_and_payment() {
        Cart cart = Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, now, now, cartDetails, CartState.CLOSED, promotionsId);

        assertNotNull(cart);
    }

    @Test
    void create_cart_when_stateIsClosed_and_UpdatedDateIsNull_shouldFail() {

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, now, null, cartDetails, CartState.CLOSED, promotionsId);
        });
    }

    @Test
    void create_cart_when_stateIsClosed_and_UpdatedDateIsNOTNull_shouldOK() {

        Cart cart = Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, now, now, cartDetails, CartState.CLOSED, promotionsId);
        assertNotNull(cart);
    }

    @Test
    void create_cart_when_stateIsActive_and_CountryTaxIsNOTNull_or_PaymentMethodIsNOTNull_shouldFail() {

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, now, now, cartDetails, CartState.ACTIVE, promotionsId);
        });

    }

    @Test
    void create_cart_when_created_Date_is_null_shouldFail() {

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, null, now, cartDetails, CartState.ACTIVE, promotionsId);
        });
    }

}
