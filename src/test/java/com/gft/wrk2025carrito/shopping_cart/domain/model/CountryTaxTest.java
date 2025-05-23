package com.gft.wrk2025carrito.shopping_cart.domain.model;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax.CountryTaxId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTaxTest {

    @Test
    void create_CountryTax_ok() {
        CountryTaxId countryTaxId = new CountryTaxId();
        String country = "Spain";
        double tax = 0.3;

        CountryTax countryTax = CountryTax.build(countryTaxId, country, tax);

        assertNotNull(countryTax);
        assertEquals(country, countryTax.getCountry());
        assertEquals(tax, countryTax.getTax());
        assertTrue(countryTax.getTax() > 0 && countryTax.getTax() < 1);
    }

    @Test
    void create_CountryTaxId_random_ok() {
        CountryTaxId id = new CountryTaxId();
        assertNotNull(id.id());
    }

    @Test
    void create_CountryTax_fail_countryTaxId_null() {
        String country = "Test";
        double tax = 0.3;

        assertThrows(IllegalArgumentException.class, () -> {
            CountryTax.build(new CountryTaxId(null), country, tax);
        });
    }

    @Test
    void create_CountryTax_fail_country_empty() {
        CountryTaxId countryTaxId = new CountryTaxId();
        String country = "";
        double tax = 0.3;

        assertThrows(IllegalArgumentException.class, () -> {
            CountryTax.build(countryTaxId, country, tax);
        });
    }

    @Test
    void create_CountryTax_fail_country_null() {
        CountryTaxId countryTaxId = new CountryTaxId();
        String country = null;
        double tax = 0.3;

        assertThrows(IllegalArgumentException.class, () -> {
            CountryTax.build(countryTaxId, country, tax);
        });
    }

    @Test
    void create_CountryTax_fail_tax_negative() {
        CountryTaxId countryTaxId = new CountryTaxId();
        String country = "Spain";
        double tax = -1;

        assertThrows(IllegalArgumentException.class, () -> {
            CountryTax.build(countryTaxId, country, tax);
        });
    }

    @Test
    void create_CountryTax_fail_tax_more_than_one() {
        CountryTaxId countryTaxId = new CountryTaxId();
        String country = "Spain";
        double tax = 1.2;

        assertThrows(IllegalArgumentException.class, () -> {
            CountryTax.build(countryTaxId, country, tax);
        });
    }
}