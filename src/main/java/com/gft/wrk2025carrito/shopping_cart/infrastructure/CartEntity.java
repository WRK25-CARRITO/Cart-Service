package com.gft.wrk2025carrito.shopping_cart.infrastructure;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder( toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartEntity {

    @EmbeddedId
    private UUID id;

    private UUID userId;
    private double totalWeight;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "country_tax_id"))
    private UUID countryTaxId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "payment_method_id"))
    private UUID paymentMethodId;

    private double totalPrice;
}
