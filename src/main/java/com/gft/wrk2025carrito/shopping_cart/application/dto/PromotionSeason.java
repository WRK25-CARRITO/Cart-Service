package com.gft.wrk2025carrito.shopping_cart.application.dto;

import lombok.*;

import java.util.List;

@Generated
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class PromotionSeason extends Promotion{

    private String name;
    private List<String> affectedCategories;

}
