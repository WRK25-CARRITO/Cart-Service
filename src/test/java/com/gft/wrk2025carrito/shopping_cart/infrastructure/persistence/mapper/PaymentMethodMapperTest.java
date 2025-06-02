package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodMapperTest {

    private final PaymentMethodMapper paymentMethodMapper = new PaymentMethodMapper();

    @Test
    void toEntity_and_back_should_preserve_values() {
        PaymentMethod domain = PaymentMethod.build(new PaymentMethodId(), "Credit Card", 0.25);

        PaymentMethodEntity entity = paymentMethodMapper.toEntity(domain);
        assertEquals(domain.getId().id(), entity.getId());
        assertEquals(domain.getPaymentMethod(), entity.getName());
        assertEquals(domain.getCharge(), entity.getCharge());

        PaymentMethod result = paymentMethodMapper.toDomain(entity);
        assertEquals(domain.getId().id(), result.getId().id());
        assertEquals(domain.getPaymentMethod(), result.getPaymentMethod());
        assertEquals(domain.getCharge(), result.getCharge());
    }
    
    @Test
    void should_ReturnNull_toEntity_ifInput_isNull() {
        assertNull(paymentMethodMapper.toEntity(null));
    }

    @Test
    void should_ReturnNull_toDomain_ifInput_isNull() {
        assertNull(paymentMethodMapper.toDomain(null));
    }

}
