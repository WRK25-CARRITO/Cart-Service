package com.gft.wrk2025carrito.shopping_cart.domain.factory;

import com.gft.wrk2025carrito.shopping_cart.domain.model.*;

import java.rmi.server.UID;
import java.util.UUID;

public class CartFactory {

    public static Cart rebuild(UUID cartId, UUID userId, double totalWeight , UUID countryTaxID, UUID paymentMethodID, double totalPrice){
        return new Cart(cartId, userId, totalWeight, new CountryTaxId(countryTaxID), new PaymentMethodId(paymentMethodID), totalPrice);
    }
}
