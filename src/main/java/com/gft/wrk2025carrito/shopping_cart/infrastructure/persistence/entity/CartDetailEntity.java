package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder( toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CART_DETAILS")
@Embeddable
public class CartDetailEntity {

    @Column(name = "PRODUCT_ID", nullable = false,columnDefinition = "CHAR(36)")
    private UUID productId;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "TOTAL_WEIGHT", nullable = false)
    private double totalWeight;

    @Column(name = "TOTAL_ITEM_PRICE", nullable = false, columnDefinition = "DECIMAL(10,3)")
    private BigDecimal totalPrice;

}
