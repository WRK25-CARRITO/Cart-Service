package com.gft.wrk2025carrito.shopping_cart.domain.model;

import lombok.Getter;

@Getter
public class CountryTax {

    private CountryTaxId id;
    private String country;
    private double tax;
}
