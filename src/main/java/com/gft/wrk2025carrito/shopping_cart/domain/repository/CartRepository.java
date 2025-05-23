package com.gft.wrk2025carrito.shopping_cart.domain.repository;

import java.util.UUID;

public interface CartRepository {

    void deleteById(UUID id);

    boolean existsById(UUID id);


}
