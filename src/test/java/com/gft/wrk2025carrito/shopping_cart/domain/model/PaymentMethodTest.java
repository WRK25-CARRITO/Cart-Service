package com.gft.wrk2025carrito.shopping_cart.domain.model;

import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethodId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    void createPaymentMethod_ok() {

        PaymentMethodId paymentMethodId = new PaymentMethodId();
        String paymentMethodName = "Test";
        double charge = 0.5;

        PaymentMethod paymentMethod = PaymentMethod.build(paymentMethodId,paymentMethodName,charge);

        assertNotNull(paymentMethod);
        assertEquals(paymentMethodId, paymentMethod.getId());
        assertEquals(charge, paymentMethod.getCharge());
        assertTrue(paymentMethod.getCharge() > 0 && paymentMethod.getCharge() < 1);
    }

    @Test
    void createPaymentMethod_fail_paymentMethodId_null() {

        String paymentMethodName = "";
        double charge = 0.5;

        assertThrows(IllegalArgumentException.class, () -> {
            PaymentMethod.build(new PaymentMethodId(null),paymentMethodName,charge);
        });
    }

    @Test
    void createPaymentMethod_fail_name_empty() {

        PaymentMethodId paymentMethodId = new PaymentMethodId();
        String paymentMethodName = "";
        double charge = 0.5;

        assertThrows(IllegalArgumentException.class, () -> {
            PaymentMethod.build(paymentMethodId,paymentMethodName,charge);
        });
    }

    @Test
    void createPaymentMethod_fail_name_null() {

        PaymentMethodId paymentMethodId = new PaymentMethodId();
        String paymentMethodName = null;
        double charge = 0.5;

        assertThrows(IllegalArgumentException.class, () -> {
            PaymentMethod.build(paymentMethodId,paymentMethodName,charge);
        });
    }

    @Test
    void createPaymentMethod_fail_charge_negative() {

        PaymentMethodId paymentMethodId = new PaymentMethodId();
        String paymentMethodName = "Test";
        double charge = -0.5;

        assertThrows(IllegalArgumentException.class, () -> {
            PaymentMethod.build(paymentMethodId,paymentMethodName,charge);
        });
    }

    @Test
    void createPaymentMethod_fail_charge_more_than_one() {

        PaymentMethodId paymentMethodId = new PaymentMethodId();
        String paymentMethodName = "Test";
        double charge = 1.5;

        assertThrows(IllegalArgumentException.class, () -> {
            PaymentMethod.build(paymentMethodId,paymentMethodName,charge);
        });
    }
}