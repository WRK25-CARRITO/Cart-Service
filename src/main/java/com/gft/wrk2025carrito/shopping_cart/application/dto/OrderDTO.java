package com.gft.wrk2025carrito.shopping_cart.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Generated
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Setter
@Getter
@ToString
public class OrderDTO {

    private UUID id;
    private UUID userId;
    private LocalDateTime creationDate;
    private BigDecimal totalPrice;
    private Double countryTax;
    private Double paymentMethod;
    private List<OrderLineDTO> orderLines;
    private List<OrderOffers> ordersOffers;
    private Boolean orderReturn;
    private Map<Long, Integer> returnedProductQuantity;

}


