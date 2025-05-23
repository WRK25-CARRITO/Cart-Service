package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.PaymentMethodMapper;

import java.util.Collections;
import java.util.List;

public class CartFactory {

    public static Cart toDomain(CartEntity cartEntity) {

        List<CartDetail> cartDetails = cartEntity.getCartDetails() != null
                ? cartEntity.getCartDetails().stream().map(CartDetailMapper::toDomain).toList()
                : Collections.emptyList();

        CountryTaxMapper.toDomain(cartEntity.getCountryTaxId());
        return Cart.build(
                new CartId(cartEntity.getId()),
                cartEntity.getUserId(),
                CountryTaxMapper.toDomain( cartEntity.getCountryTaxId()),
                PaymentMethodMapper.toDomain( cartEntity.getPaymentMethodId()),
                cartEntity.getTotalPrice(),
                cartEntity.getTotalWeight(),
                cartEntity.getCreatedAt(),
                cartEntity.getUpdatedAt(),
                cartDetails,
                cartEntity.getState(),
                cartEntity.getPromotionIds() == null ? Collections.emptyList() : cartEntity.getPromotionIds().stream().toList()
        );
    }

    public static CartEntity toEntity(Cart cart) {

        List<CartDetailEntity> cartDetails = cart.getCartDetails().stream().map(CartDetailMapper::toEntity).toList();


        CountryTaxEntity countryTaxEntity = CountryTaxMapper.toEntity(cart.getCountryTax());
        PaymentMethodEntity paymentMethodEntity = PaymentMethodMapper.toEntity(cart.getPaymentMethod());

        return CartEntity.builder()
                .id(cart.getId().id())
                .userId(cart.getUserId())
                .countryTaxId(countryTaxEntity)
                .paymentMethodId(paymentMethodEntity)
                .totalPrice(cart.getTotalPrice())
                .totalWeight(cart.getTotalWeight())
                .updatedAt(cart.getUpdatedAt())
                .createdAt(cart.getCreatedAt())
                .cartDetails(cartDetails)
                .state(cart.getState())
                .promotionIds(cart.getPromotionsId() == null ? Collections.emptyList() : cart.getPromotionsId().stream().toList())
                .build();
    }


}