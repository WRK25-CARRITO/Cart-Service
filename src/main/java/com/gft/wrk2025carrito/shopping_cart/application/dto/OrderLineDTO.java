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
    private Long productId;
    private Integer quantity;
    private Double lineWeight;
    private Double productPrice;
    private BigDecimal LinePrice;
    private Integer returnQuality;

    private static OrderLineDTO fromCartDetail(CartDetail detail) {
        if (detail == null) {
            return null;
        }

        return OrderLineDTO.builder()
                .productId(detail.getProductId())
                .quantity(detail.getQuantity())
                .lineWeight(detail.getTotalWeight())
                .productPrice(null)
                .LinePrice(detail.getTotalPrice())
                .returnQuality(null)
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
