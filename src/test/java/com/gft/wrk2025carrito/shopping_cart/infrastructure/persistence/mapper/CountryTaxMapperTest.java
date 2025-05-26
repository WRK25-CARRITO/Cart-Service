package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper;

import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTaxId;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CountryTaxEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CountryTaxMapperTest {

    private final CountryTaxMapper countryTaxMapper = new CountryTaxMapper();

    @Test
    void toEntity_and_back_should_preserve_values() {
        CountryTax domain = CountryTax.build(new CountryTaxId(), "Spain", 0.21);

        CountryTaxEntity entity = countryTaxMapper.toEntity(domain);
        assertEquals(domain.getId().id(), entity.getId());
        assertEquals(domain.getCountry(), entity.getCountry());
        assertEquals(domain.getTax(), entity.getTax());

        CountryTax result = countryTaxMapper.toDomain(entity);
        assertEquals(domain.getId().id(), result.getId().id());
        assertEquals(domain.getCountry(), result.getCountry());
        assertEquals(domain.getTax(), result.getTax());
    }

    @Test
    void toEntity_should_return_null_if_input_is_null() {
        assertNull(countryTaxMapper.toEntity(null));
    }

    @Test
    void toDomain_should_return_null_if_input_is_null() {
        assertNull(countryTaxMapper.toDomain(null));
    }


}
