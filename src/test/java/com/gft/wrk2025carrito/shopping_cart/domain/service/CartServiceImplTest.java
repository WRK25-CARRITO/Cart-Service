package com.gft.wrk2025carrito.shopping_cart.domain.service;


import com.gft.wrk2025carrito.shopping_cart.application.service.impl.CartServiceImpl;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CartState;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartServiceImpl;

    @Mock
    private CartRepositoryImpl cartRepository;

    private Cart newCart;
    private CartEntity newCartEntity;
    private final List<CartDetail> newCartDetails = new ArrayList<>();
    private final List<UUID> newCartPromotionIds = new ArrayList<>();

    private CartEntity presentCart;

    @BeforeEach
    void setUp() {

        newCart = Cart.build(
            new CartId(),
            UUID.randomUUID(),
            null,
            null,
            BigDecimal.valueOf(1.0),
            5.0D,
            new Date(),
            new Date(),
            newCartDetails,
            CartState.ACTIVE, newCartPromotionIds
        );

        newCartEntity = new CartEntity();
            newCartEntity.setId(UUID.randomUUID());
            newCartEntity.setCountryTaxId(null);
            newCartEntity.setPaymentMethodId(null);
            newCartEntity.setCreatedAt(new Date());
            newCartEntity.setUpdatedAt(new Date());
            newCartEntity.setState(CartState.ACTIVE);
            newCartEntity.setCartDetails(new ArrayList<>());

        presentCart = new CartEntity();
            presentCart.setId(UUID.randomUUID());
            presentCart.setState(CartState.ACTIVE);
            presentCart.setCreatedAt(new Date());
            presentCart.setUpdatedAt(new Date());
            presentCart.setCartDetails(new ArrayList<>());

    }

    @Test
    void update_Cart_shouldOK() {

        when(cartRepository.existsByCartId(newCart.getId())).thenReturn(false);

        CartEntity cartEntity = cartServiceImpl.update(newCart);

        assertEquals(cartEntity, presentCart);
    }
}
