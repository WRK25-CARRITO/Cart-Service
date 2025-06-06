package com.gft.wrk2025carrito.shopping_cart.application.dto;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Generated
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineDTO {
    private Long product;
    private Integer quantity;
    private Double lineWeight;
    private BigDecimal linePrice;
    private Integer returnedQuantity;

    private static OrderLineDTO fromCartDetail(CartDetail detail) {
        if (detail == null) {
            return null;
        }

        return OrderLineDTO.builder()
                .product(detail.getProductId())
                .quantity(detail.getQuantity())
                .lineWeight(detail.getTotalWeight())
                .linePrice(detail.getTotalPrice())
                .returnedQuantity(null)
                .build();
    }

    public static List<OrderLineDTO> fromCartDetailList(List<CartDetail> details) {
        if (details == null) {
            return List.of();
        }
        return details.stream()
                .map(OrderLineDTO::fromCartDetail)
                .collect(Collectors.toList());
    }
}
