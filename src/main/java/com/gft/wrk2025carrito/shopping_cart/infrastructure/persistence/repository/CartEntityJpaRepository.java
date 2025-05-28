package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository;

import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CartEntityJpaRepository extends JpaRepository<CartEntity, UUID> {

    void deleteById(UUID id);

    void deleteAllByUserId(UUID userId);

    List<CartEntity> findByUserId(UUID userId);
}
