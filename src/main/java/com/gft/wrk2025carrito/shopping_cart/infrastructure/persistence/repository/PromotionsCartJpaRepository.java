package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository;

import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PaymentMethodEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.PromotionsCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionsCartJpaRepository extends JpaRepository<PromotionsCartEntity, UUID> {

    List<PromotionsCartEntity> getAllByCart_id(UUID id);
}
