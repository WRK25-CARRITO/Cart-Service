package com.gft.wrk2025carrito.shopping_cart.application.dto;

import lombok.*;
import java.util.UUID;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOffers {

    private UUID orderId;
    private Long offerId;

}

