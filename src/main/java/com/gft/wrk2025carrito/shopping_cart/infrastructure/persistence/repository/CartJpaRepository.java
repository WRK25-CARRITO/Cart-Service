package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<CartEntity, CartId> {

}
