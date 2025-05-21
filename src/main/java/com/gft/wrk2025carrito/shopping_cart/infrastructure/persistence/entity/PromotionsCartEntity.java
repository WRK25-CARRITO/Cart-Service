package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity;

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
@Table(name = "PROMOTIONS_CART")
public class PromotionsCartEntity {

    @Id
    @Column(name = "ID_PROMOTION", nullable = false)
    private UUID id_promotion;


    @JoinTable(name = "CARTS", joinColumns = @JoinColumn(name="CART_ID"))
    @Column(name = "CART_ID", nullable = false)
    private UUID cart_id;


}
