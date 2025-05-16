package com.gft.wrk2025carrito.shopping_cart.domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @Test
    void create_cart_ok() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();

        Cart cart = Cart.build(cartId,userId, countryTaxId, paymentMethodId, 10.0,100.0,cartDetails);

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
        UUID userId = UUID.randomUUID();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(new CartId(null), userId, countryTaxId, paymentMethodId, 10.0, 5.0, cartDetails);
        });
    }

    @Test
    void create_cart_fail_negative_weight() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTaxId, paymentMethodId, 10.0, -5.0, cartDetails);
        });
    }

    @Test
    void create_cart_fail_negative_price() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();


        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTaxId, paymentMethodId, -100.0, 5.0, cartDetails);
        });
    }

    @Test
    void create_cart_fail_cartDetails_null() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = null;


        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTaxId, paymentMethodId, 100.0, 5.0, cartDetails);
        });
    }

}
