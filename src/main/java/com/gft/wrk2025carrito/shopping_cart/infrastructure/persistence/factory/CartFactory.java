package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.PaymentMethodMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartFactory {

    private final CountryTaxMapper countryTaxMapper;
    private final PaymentMethodMapper paymentMethodMapper;

    public Cart toDomain(CartEntity cartEntity) {

        List<CartDetail> cartDetails = cartEntity.getCartDetails() != null
                ? cartEntity.getCartDetails().stream().map(CartDetailMapper::toDomain).toList()
                : Collections.emptyList();

        return Cart.build(
                new CartId(cartEntity.getId()),
                cartEntity.getUserId(),
                countryTaxMapper.toDomain(cartEntity.getCountryTax()),
                paymentMethodMapper.toDomain(cartEntity.getPaymentMethod()),
                cartEntity.getTotalPrice(),
                cartEntity.getTotalWeight(),
                cartEntity.getCreatedAt(),
                cartEntity.getUpdatedAt(),
                cartDetails,
                cartEntity.getState(),
                cartEntity.getPromotionIds()
                );
    }

    public CartEntity toEntity(Cart cart) {

        List<CartDetailEntity> cartDetails =cart.getCartDetails().stream().map(CartDetailMapper::toEntity).toList();

        CountryTaxEntity countryTaxEntity = countryTaxMapper.toEntity(cart.getCountryTax());
        PaymentMethodEntity paymentMethodEntity = paymentMethodMapper.toEntity(cart.getPaymentMethod());

        return CartEntity.builder()
                .id(cart.getId().id())
                .userId(cart.getUserId())
                .countryTax(countryTaxEntity)
                .paymentMethod(paymentMethodEntity)
                .totalPrice(cart.getTotalPrice())
                .totalWeight(cart.getTotalWeight())
                .cartDetails(cartDetails)
                .state(cart.getState())
                .promotionIds(cart.getPromotionIds())
                .build();
    }
}
