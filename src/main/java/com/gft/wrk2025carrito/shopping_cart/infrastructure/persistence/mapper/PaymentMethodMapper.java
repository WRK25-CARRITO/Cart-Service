package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;

public class PaymentMethodMapper {

    public static PaymentMethod toDomain(PaymentMethodEntity paymentMethodEntity) {

        if(paymentMethodEntity == null){
            return null;
        }

        return PaymentMethod.build(
                new PaymentMethodId(paymentMethodEntity.getId()),
                paymentMethodEntity.getName(),
                paymentMethodEntity.getCharge());
    }

    public static PaymentMethodEntity toEntity(PaymentMethod paymentMethodDomain) {

        if(paymentMethodDomain == null){
            return null;
        }

        return PaymentMethodEntity.builder()
                .id(paymentMethodDomain.getId().id())
                .name(paymentMethodDomain.getPaymentMethod())
                .charge(paymentMethodDomain.getCharge())
                .build();
    }


}
