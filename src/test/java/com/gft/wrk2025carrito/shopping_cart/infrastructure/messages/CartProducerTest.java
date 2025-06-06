package com.gft.wrk2025carrito.shopping_cart.infrastructure.messages;

import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTORecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CartProducer cartProducer;

    @Test
    void sendCartStateChanged_shouldInvokeRabbitTemplateWithCorrectArguments() {
        UUID userId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        String state = "PENDING";
        OrderDTORecord orderDTO = new OrderDTORecord(userId, cartId, state);

        cartProducer.sendCartStateChanged(orderDTO);

        verify(rabbitTemplate, times(1))
                .convertAndSend("orders", "state.changed", orderDTO);

        verifyNoMoreInteractions(rabbitTemplate);
    }
}
