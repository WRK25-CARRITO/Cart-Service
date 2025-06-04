package com.gft.wrk2025carrito.shopping_cart.application.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.Date;

@Generated
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "promotionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PromotionSeason.class, name = "SEASON"),
        @JsonSubTypes.Type(value = PromotionQuantity.class, name = "QUANTITY")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Promotion {

    private Long id;
    private Date startDate;
    private Date endDate;
    private Double discount;
    private String promotionType;

}
