package com.gft.wrk2025carrito.shopping_cart.domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    private final Date creationDate = new Date();
    private final Date updateDate = new Date();
    private final CountryTax countryTax = CountryTax.build(new CountryTaxId(), "Spain", 0.21);
    private final PaymentMethod paymentMethod = PaymentMethod.build(new PaymentMethodId(), "VISA", 0.02);

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
        assertEquals(10.0, cart.getTotalPrice());
    }

    @Test
    void create_CartId_random_ok() {
        CartId cartId = new CartId();
        assertNotNull(cartId.id());
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
    void create_cart_success_closed_with_tax_and_payment() {
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
                CartState.CLOSED,
                new ArrayList<>()
        );
        assertNotNull(cart);
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
    void create_cart_fail_paymentMethod_not_null_when_state_not_pending_or_closed() {
        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, null, null, BigDecimal.valueOf(100.0), 5.0, creationDate, updateDate, cartDetails, CartState.ACTIVE, promotionsId);
        });
    }


    @Test
    void create_cart_success_closed_with_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        Cart cart = Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, creationDate, updateDate, cartDetails, CartState.CLOSED, promotionsId);

        assertNotNull(cart);
    }

    @Test
    void create_cart_success_pending_with_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        Cart cart = Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, creationDate, updateDate, cartDetails, CartState.PENDING, promotionsId);

        assertNotNull(cart);
    }

    @Test
    void create_cart_success_pending_without_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        Cart cart = Cart.build(cartId, userId, null, null, BigDecimal.valueOf(10.0), 5.0, creationDate, updateDate, cartDetails, CartState.PENDING, promotionsId);

        assertNotNull(cart);
    }

    @Test
    void create_cart_fail_non_pending_or_closed_with_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, creationDate, updateDate, cartDetails, CartState.ACTIVE, promotionsId);
        });
    }

    @Test
    void create_cart_fail_null_state() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, creationDate, updateDate, cartDetails, null, promotionsId);
        });
    }

    @Test
    void create_cart_when_stateIsClosed_and_UpdatedDateIsNull_shouldFail() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, creationDate, null, cartDetails, CartState.CLOSED, promotionsId);
        });
    }

    @Test
    void create_cart_when_stateIsClosed_and_UpdatedDateIsNOTNull_shouldOK() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        Cart cart = Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, creationDate, updateDate, cartDetails, CartState.CLOSED, promotionsId);
        assertNotNull(cart);
    }

    @Test
    void create_cart_when_stateIsActive_and_CountryTaxIsNOTNull_or_PaymentMethodIsNOTNull_shouldFail() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, creationDate, updateDate, cartDetails, CartState.ACTIVE, promotionsId);
        });

    }

    @Test
    void create_cart_when_created_Date_is_null_shouldFail() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();


        List<CartDetail> cartDetails = new ArrayList<>();
        List<UUID> promotionsId = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTax, paymentMethod, BigDecimal.valueOf(10.0), 5.0, null, updateDate, cartDetails, CartState.ACTIVE, promotionsId);
        });
    }

}
