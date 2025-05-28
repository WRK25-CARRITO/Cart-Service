package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartDetailEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.PaymentMethodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartFactory {

    private final CountryTaxMapper countryTaxMapper;
    private final PaymentMethodMapper paymentMethodMapper;
    private final CartDetailMapper cartDetailMapper;

    public Cart toDomain(CartEntity cartEntity) {

        List<CartDetail> cartDetails = cartEntity.getCartDetails() != null
                ? cartEntity.getCartDetails().stream().map(cartDetailMapper::toDomain).toList()
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

        List<CartDetailEntity> cartDetails =cart.getCartDetails().stream().map(cartDetailMapper::toEntity).toList();

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
