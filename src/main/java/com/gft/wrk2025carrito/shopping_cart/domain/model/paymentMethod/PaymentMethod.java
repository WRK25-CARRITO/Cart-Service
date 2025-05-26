package com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMethod {

    private PaymentMethodId id;
    private String paymentMethod;
    private double charge;

    public static PaymentMethod build(PaymentMethodId id, String paymentMethod, double charge) {

        if(paymentMethod == null || paymentMethod.isEmpty()){
            throw new IllegalArgumentException("Payment Method name cannot be null or empty");
        }

        if(charge < 0 || charge > 1) {
            throw new IllegalArgumentException("Charge must be between 0 and 1");
        }

        return new PaymentMethod(id, paymentMethod, charge);
    }
}
