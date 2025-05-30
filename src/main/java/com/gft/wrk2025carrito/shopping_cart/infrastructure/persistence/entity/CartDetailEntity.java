package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder( toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CartDetailEntity {

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

}
