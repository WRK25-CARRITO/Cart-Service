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
@Entity
@Table(name = "PAYMENT_METHODS")
public class PaymentMethodEntity {

    @Id
    @Column(name="ID")
    private UUID id;

    @Column(name="NAME")
    private String name;

    @Column(name = "CHARGE")
    private Double charge;

}
