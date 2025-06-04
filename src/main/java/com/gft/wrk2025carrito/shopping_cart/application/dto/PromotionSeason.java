package com.gft.wrk2025carrito.shopping_cart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Generated
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionSeason extends Promotion{

    private String name;
    private List<String> affectedCategories;

}