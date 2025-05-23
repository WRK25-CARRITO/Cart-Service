package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.repository.PaymentMethodRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.PaymentMethodEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class PaymentMethodEntityRepositoryImpl implements PaymentMethodRepository {

    private final PaymentMethodEntityJpaRepository jpaRepository;
}
