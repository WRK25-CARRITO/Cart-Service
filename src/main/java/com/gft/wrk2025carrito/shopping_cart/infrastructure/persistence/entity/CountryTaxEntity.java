package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder( toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "COUNTRY_TAXES")
@Entity
public class CountryTaxEntity {

    @Id
    private UUID id;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name= "TAX")
    private double tax;
}
