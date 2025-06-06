package com.gft.wrk2025carrito.shopping_cart.infrastructure.messages;

import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartProducer {
    private final RabbitTemplate rabbitTemplate;

    private final String exchange = "orders";
    private final String routingKey = "state.changed";

    public void sendCartStateChanged(OrderDTO orderDTO) {
        rabbitTemplate.convertAndSend(exchange, routingKey, orderDTO);
    }

}
