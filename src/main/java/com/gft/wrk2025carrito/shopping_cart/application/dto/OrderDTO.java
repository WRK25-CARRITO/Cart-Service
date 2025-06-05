package com.gft.wrk2025carrito.shopping_cart.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Generated
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
public class OrderDTO {

    private UUID orderId;
    private UUID userId;
    private Date orderDate;
    private BigDecimal totalPrice;
    private Double countryTax;
    private Double paymentMethod;
    private List<OrderLineDTO> orderLines;
    private List<Long> ordersOffers;
    private Boolean orderReturn;

}
