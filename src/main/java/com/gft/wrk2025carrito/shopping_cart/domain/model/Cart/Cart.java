package com.gft.wrk2025carrito.shopping_cart.domain.model.Cart;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.model.PaymentMethod.PaymentMethodId;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {

    private CartId id;
    private UUID userId;
    private CountryTax countryTax;
    private PaymentMethod paymentMethod;
    private BigDecimal totalPrice;
    private double totalWeight;
    private Date createdAt;
    private Date updatedAt;
    private List<CartDetail> cartDetails;
    private CartState state;
    private List<UUID> promotionsId;

    public static Cart build(CartId id, UUID userId, CountryTax countryTax, PaymentMethod paymentMethod, BigDecimal totalPrice, double totalWeight, Date createdAt, Date updatedAt, List<CartDetail> cartDetails, CartState state, List<UUID> idPromotions) {
        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (totalWeight < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }

        if(cartDetails == null) {
            throw new IllegalArgumentException("Cart details cannot be null");
        }

        if(state == null) {
            throw new IllegalArgumentException("Cart details cannot be null");
        }

        if(createdAt == null) {
            throw new IllegalArgumentException("Created date cannot be null");
        }

        if(state == CartState.CLOSED && updatedAt == null) {
            throw new IllegalArgumentException("Update date cannot be null if cart state is CLOSED");
        }

        if (state != CartState.PENDING && state != CartState.CLOSED) {
            if (countryTax != null || paymentMethod != null) {
                throw new IllegalArgumentException("Country Tax and Payment method must both be null unless state is PENDING or CLOSED");
            }
        }

        return new Cart(id, userId, countryTax, paymentMethod, totalPrice, totalWeight, createdAt, updatedAt,  cartDetails , state, idPromotions);
    }

}
