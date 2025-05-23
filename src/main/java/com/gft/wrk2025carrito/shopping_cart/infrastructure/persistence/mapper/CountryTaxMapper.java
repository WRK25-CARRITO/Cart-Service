package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryTaxMapper {

    public CountryTaxEntity toEntity(CountryTax countryTax) {

        if (countryTax == null) return null;

        return CountryTaxEntity.builder()
                .id(countryTax.getId().id())
                .country(countryTax.getCountry())
                .tax(countryTax.getTax())
                .build();
    }

    public CountryTax toDomain(CountryTaxEntity countryTaxEntity) {

        if (countryTaxEntity == null) return null;

        return CountryTax.build(
                new CountryTaxId(countryTaxEntity.getId()),
                countryTaxEntity.getCountry(),
                countryTaxEntity.getTax()
        );

    }
}
