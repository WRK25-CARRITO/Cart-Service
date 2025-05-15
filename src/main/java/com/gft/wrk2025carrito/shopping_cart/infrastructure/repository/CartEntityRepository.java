package com.gft.wrk2025carrito.shopping_cart.infrastructure.repository;

import com.gft.wrk2025carrito.shopping_cart.infrastructure.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartEntityRepository extends JpaRepository<CartEntity, UUID>{

}
