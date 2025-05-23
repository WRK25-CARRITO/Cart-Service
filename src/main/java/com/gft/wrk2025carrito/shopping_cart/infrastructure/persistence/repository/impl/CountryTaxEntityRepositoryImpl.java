package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;

import com.gft.wrk2025carrito.shopping_cart.domain.repository.CountryTaxRepository;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.CountryTaxEntityJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class CountryTaxEntityRepositoryImpl implements CountryTaxRepository {

    private final CountryTaxEntityJpaRepository countryTaxEntityJpaRepository;
}
