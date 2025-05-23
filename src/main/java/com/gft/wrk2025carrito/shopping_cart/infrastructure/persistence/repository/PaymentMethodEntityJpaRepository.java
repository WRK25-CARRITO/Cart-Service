package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository;

import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentMethodEntityJpaRepository extends JpaRepository<PaymentMethodEntity, UUID> {
}
