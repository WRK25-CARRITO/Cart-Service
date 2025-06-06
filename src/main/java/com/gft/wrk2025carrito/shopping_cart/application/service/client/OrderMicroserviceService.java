package com.gft.wrk2025carrito.shopping_cart.application.service.client;

import java.util.List;
import java.util.Map;

public interface OrderMicroserviceService {

    List<Long> getAllOrderPromotions(Map<Long,Integer> cartDetailProducts);
}
