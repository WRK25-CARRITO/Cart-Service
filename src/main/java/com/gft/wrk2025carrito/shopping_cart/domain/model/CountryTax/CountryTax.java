package com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTax;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryTax {

    private CountryTaxId id;
    private String country;
    private double tax;

    public static CountryTax build(CountryTaxId id, String country, double tax) {

        if(country == null || country.isEmpty()){
            throw new IllegalArgumentException("Country cannot be null or empty");
        }

        if(tax < 0 || tax > 1) {
            throw new IllegalArgumentException("Tax must be between 0 and 1");
        }

        return new CountryTax(id, country, tax);
    }
}
