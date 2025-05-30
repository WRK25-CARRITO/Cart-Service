package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.PaymentMethodMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartFactoryTest {

    @Mock
    private CountryTaxMapper countryTaxMapper;

    @Mock
    private PaymentMethodMapper paymentMethodMapper;

    @Mock
    private CartDetailMapper cartDetailMapper;

    @InjectMocks
    private CartFactory cartFactory;

    @Test
    void toDomain_with_valid_entity_returns_cart() {
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CountryTaxEntity taxEntity = CountryTaxEntity.builder()
                .id(UUID.randomUUID())
                .country("ES")
                .tax(0.2)
                .build();

        PaymentMethodEntity paymentEntity = PaymentMethodEntity.builder()
                .id(UUID.randomUUID())
                .name("test")
                .charge(0.3)
                .build();

        CartEntity entity = CartEntity.builder()
                .id(cartId)
                .userId(userId)
                .countryTax(taxEntity)
                .paymentMethod(paymentEntity)
                .createdAt(new Date())
                .updatedAt(new Date())
                .cartDetails(Collections.emptyList())
                .state(CartState.PENDING)
                .promotionIds(Collections.emptyList())
                .build();

        CountryTax expectedTax = CountryTax.build(new CountryTaxId(), "ES", 0.2);
        PaymentMethod expectedPayment = PaymentMethod.build(new PaymentMethodId(), "test", 0.3);

        when(countryTaxMapper.toDomain(taxEntity)).thenReturn(expectedTax);
        when(paymentMethodMapper.toDomain(paymentEntity)).thenReturn(expectedPayment);

        Cart result = cartFactory.toDomain(entity);

        assertEquals(cartId, result.getId().id());
        assertEquals(userId, result.getUserId());
        assertEquals(expectedTax, result.getCountryTax());
        assertEquals(expectedPayment, result.getPaymentMethod());
    }


    @Test
    void toDomain_with_null_cartDetails_returns_empty_list() {
        CartEntity entity = CartEntity.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .countryTax(null)
                .paymentMethod(null)
                .createdAt(new Date())
                .updatedAt(new Date())
                .cartDetails(null)
                .state(CartState.PENDING)
                .promotionIds(null)
                .build();

        Cart cart = cartFactory.toDomain(entity);

        assertNotNull(cart);
        assertTrue(cart.getCartDetails().isEmpty());
    }

    @Test
    void toEntity_with_valid_cart_returns_entity() {
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CountryTax tax = CountryTax.build(new CountryTaxId(), "ES", 0.2);
        PaymentMethod payment = PaymentMethod.build(new PaymentMethodId(), "VISA", 0.3);

        Cart cart = Cart.build(
                new CartId(cartId),
                userId,
                tax,
                payment,
                BigDecimal.TEN,
                5.0,
                new Date(),
                new Date(),
                Collections.emptyList(),
                CartState.PENDING,
                Collections.emptyList()
        );

        when(countryTaxMapper.toEntity(tax)).thenReturn(new CountryTaxEntity());
        when(paymentMethodMapper.toEntity(payment)).thenReturn(new PaymentMethodEntity());

        CartEntity entity = cartFactory.toEntity(cart);

        assertEquals(cartId, entity.getId());
        assertEquals(userId, entity.getUserId());

    }

    @Test
    void toEntity_with_null_cartDetails_returns_empty_list() {
        Cart cart = Cart.build(
                new CartId(),
                UUID.randomUUID(),
                null,
                null,
                BigDecimal.TEN,
                5.0,
                new Date(),
                new Date(),
                null,
                CartState.ACTIVE,
                null
        );

        CartEntity entity = cartFactory.toEntity(cart);

        assertNotNull(entity);
        assertTrue(entity.getCartDetails().isEmpty());
    }
}