package com.gft.wrk2025carrito.shopping_cart.application.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Generated
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Product {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Double weight;
}
