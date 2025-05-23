package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethodId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.*;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CartJpaRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CountryTaxJpaRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.PaymentMethodJpaRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.PromotionsCartJpaRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class CartFactory {

    private static CartJpaRepository cartJpaRepository;
    private static CountryTaxJpaRepository countryTaxJpaRepository;
    private static PaymentMethodJpaRepository paymentMethodJpaRepository;
    private static PromotionsCartJpaRepository promotionsCartJpaRepository;

    public static Cart toDomain(CartEntity cartEntity) {

        List<CartDetail> cartDetails = cartEntity.getCartDetails() != null
                ? cartEntity.getCartDetails().stream().map(CartDetailMapper::toDomain).toList()
                : Collections.emptyList();

        return Cart.build(
            new CartId(
                cartEntity.getId()),
                cartEntity.getUserId(),
                new CountryTaxId(cartEntity.getCountryTaxId().getId()),
                new PaymentMethodId(cartEntity.getPaymentMethodId().getId()),
                BigDecimal.valueOf(cartEntity.getTotalPrice().toBigInteger().doubleValue()),
                cartEntity.getTotalWeight(),
                cartEntity.getCreatedAt(),
                cartEntity.getUpdatedAt(),
                cartDetails,
                cartEntity.getState(),
                cartEntity.getPromotionIds()
            );
    }

    public static CartEntity toEntity(Cart cartDomain) {

        List<CartDetailEntity> cartDetails = cartDomain.getCartDetails() != null
                ? cartDomain.getCartDetails().stream().map(CartDetailMapper::toEntity).toList()
                : Collections.emptyList();

        CountryTaxEntity countryTaxEntity = null;
        PaymentMethodEntity paymentMethodEntity = null;
        try{
            countryTaxEntity = countryTaxJpaRepository.findById(cartDomain.getCountryTaxId().id()).orElse(null);
            paymentMethodEntity = paymentMethodJpaRepository.findById(cartDomain.getPaymentMethodId().id()).orElse(null);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }

        return CartEntity.builder()
                .id(cartDomain.getId().id())
                .userId(cartDomain.getUserId())
                .countryTaxId(countryTaxEntity)
                .paymentMethodId(paymentMethodEntity)
                .totalPrice(BigDecimal.valueOf(cartDomain.getTotalPrice().doubleValue()))
                .totalWeight(cartDomain.getTotalWeight())
                .cartDetails(cartDetails)
                .state(cartDomain.getState())
                .promotionIds(cartDomain.getPromotionsId())
                .build();
    }
}
