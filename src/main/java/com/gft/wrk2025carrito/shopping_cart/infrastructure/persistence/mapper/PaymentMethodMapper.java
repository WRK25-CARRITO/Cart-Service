package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodMapper {

    public PaymentMethodEntity toEntity(PaymentMethod paymentMethod) {

        if (paymentMethod == null) return null;

        return PaymentMethodEntity.builder()
                .id(paymentMethod.getId().id())
                .name(paymentMethod.getPaymentMethod())
                .charge(paymentMethod.getCharge())
                .build();
    }

    public PaymentMethod toDomain(PaymentMethodEntity paymentMethodEntity) {

        if (paymentMethodEntity == null) return null;

        return PaymentMethod.build(
                new PaymentMethodId(paymentMethodEntity.getId()),
                paymentMethodEntity.getName(),
                paymentMethodEntity.getCharge()
        );

    }
}
