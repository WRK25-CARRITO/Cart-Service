package com.gft.wrk2025carrito.shopping_cart.domain.model.cart;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Generated
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Cart {

    private CartId id;
    private UUID userId;
    private CountryTax countryTax;
    private PaymentMethod paymentMethod;
    private BigDecimal totalPrice;
    private Double totalWeight;
    private Date createdAt;
    private Date updatedAt;
    private List<CartDetail> cartDetails;
    private CartState state;
    private List<Long> promotionIds;

    public static Cart build(CartId id, UUID userId, CountryTax countryTax, PaymentMethod paymentMethod, BigDecimal totalPrice, Double totalWeight, Date createdAt , Date updatedAt, List<CartDetail> cartDetails, CartState state, List<Long> idPromotion) {

        if (totalPrice != null && totalPrice.doubleValue() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (totalWeight != null && totalWeight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if(cartDetails == null) {
            cartDetails = Collections.emptyList();
        }

        if(state == null) {
            throw new IllegalArgumentException("Cart details cannot be null");
        }

        if (state != CartState.PENDING && state != CartState.CLOSED) {
            if (countryTax != null || paymentMethod != null) {
                throw new IllegalArgumentException("Country Tax and Payment method must be null unless state is PENDING or CLOSED");
            }
        }

        if(createdAt == null) {
            throw new IllegalArgumentException("Cart createdAt date cannot be null");
        }

        if(updatedAt == null && state == CartState.CLOSED) {
            throw new IllegalArgumentException("Cart updatedAt date cannot be null when cart state is CLOSED");
        }

        return new Cart(id, userId,  countryTax, paymentMethod, totalPrice, totalWeight,  createdAt, updatedAt, cartDetails, state, idPromotion);
    }

}
