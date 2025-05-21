package com.gft.wrk2025carrito.shopping_cart.domain.model;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethodId;
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

        Cart cart = Cart.build(cartId,userId, null, null, 10.0,100.0,cartDetails, CartState.ACTIVE);

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
            Cart.build(new CartId(null), userId, null, null, 10.0, 5.0, cartDetails,CartState.ACTIVE);
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
            Cart.build(cartId, userId, null, null, 10.0, -5.0, cartDetails, CartState.PENDING);
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
            Cart.build(cartId, userId, null, null, -100.0, 5.0, cartDetails, CartState.ACTIVE);
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
            Cart.build(cartId, userId, null, null, 100.0, 5.0, cartDetails, CartState.ACTIVE);
        });
    }


    @Test
    void create_cart_success_closed_with_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();

        Cart cart = Cart.build(cartId, userId, countryTaxId, paymentMethodId, 10.0, 5.0, cartDetails, CartState.CLOSED);

        assertNotNull(cart);
    }

    @Test
    void create_cart_success_pending_with_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();

        Cart cart = Cart.build(cartId, userId, countryTaxId, paymentMethodId, 10.0, 5.0, cartDetails, CartState.PENDING);

        assertNotNull(cart);
    }

    @Test
    void create_cart_success_pending_without_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        List<CartDetail> cartDetails = new ArrayList<>();

        Cart cart = Cart.build(cartId, userId, null, null, 10.0, 5.0, cartDetails, CartState.PENDING);

        assertNotNull(cart);
    }

    @Test
    void create_cart_fail_non_pending_or_closed_with_tax_and_payment() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTaxId, paymentMethodId, 10.0, 5.0, cartDetails, CartState.ACTIVE);
        });
    }

    @Test
    void create_cart_fail_null_state() {
        UUID userId = UUID.randomUUID();
        CartId cartId = new CartId();
        CountryTaxId countryTaxId = new CountryTaxId();
        PaymentMethodId paymentMethodId = new PaymentMethodId();
        List<CartDetail> cartDetails = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            Cart.build(cartId, userId, countryTaxId, paymentMethodId, 10.0, 5.0, cartDetails, null);
        });
    }

}
