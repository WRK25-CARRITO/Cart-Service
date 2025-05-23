package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;

public class CountryTaxMapper {

    public static CountryTaxEntity toEntity(CountryTax countryTaxDomain) {

        if (countryTaxDomain == null) {
            return null;
        }

        return CountryTaxEntity.builder()
                .id(countryTaxDomain.getId().id())
                .country(countryTaxDomain.getCountry())
                .tax(countryTaxDomain.getTax())
                .build();
    }

    public static CountryTax toDomain(CountryTaxEntity countryTaxEntity) {

        if(countryTaxEntity == null){
            return null;
        }

        return CountryTax.build(
                new CountryTaxId(countryTaxEntity.getId()) ,
                countryTaxEntity.getCountry(),
                countryTaxEntity.getTax()
        );
    }

}
