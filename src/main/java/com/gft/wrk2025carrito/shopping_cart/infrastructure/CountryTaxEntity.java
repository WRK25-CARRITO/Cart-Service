package com.gft.wrk2025carrito.shopping_cart.infrastructure;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CountryTaxId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CountryTaxEntity {

    @EmbeddedId
    CountryTaxId id;

    String country;

    double tax;

}
