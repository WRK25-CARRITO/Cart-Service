package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;

import java.math.BigDecimal;
import java.util.List;

public class CartFactory {

    public static Cart toDomain(CartEntity cartEntity) {

        List<CartDetail> cartDetails = cartEntity.getCartDetails().stream()
            .map(CartDetailMapper::toDomain)
            .toList();

        return Cart.build(
            new CartId(
                cartEntity.getId()),
                cartEntity.getUserId(),
                new CountryTaxId(cartEntity.getCountryTaxId()),
                new PaymentMethodId(cartEntity.getPaymentMethodId()),
                cartEntity.getTotalPrice().toBigInteger().doubleValue(),
                cartEntity.getTotalWeight(),
                cartEntity.getCreatedAt(),
                cartEntity.getUpdatedAt(),
                cartDetails,
                cartEntity.getState(),
                cartEntity.getPromotionsId()
            );
    }

    public static CartEntity toEntity(Cart cart) {

        List<CartDetailEntity> cartDetails = cart.getCartDetails().stream()
                .map(CartDetailMapper::toEntity)
                .toList();

        return CartEntity.builder()
                .id(cart.getId().id())
                .userId(cart.getUserId())
                .countryTaxId(cart.getCountryTaxId().id())
                .paymentMethodId(cart.getPaymentMethodId().id())
                .totalPrice(BigDecimal.valueOf(cart.getTotalPrice()))
                .totalWeight(cart.getTotalWeight())
                .cartDetails(cartDetails)
                .state(cart.getState())
                .promotionsId(cart.getPromotionsId())
                .build();
    }
}
