package com.gft.wrk2025carrito.shopping_cart.infrastructure.messages;

import com.gft.wrk2025carrito.shopping_cart.application.service.CartServicesImpl;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.dto.StockNotificationResponse;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.dto.UserProductStockChangedNotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQProductStockChangedConsumerTest {

    @Mock
    private CartServicesImpl cartServices;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQProductStockChangedConsumer consumer;

    @Test
    void shouldSendNotificationIfProductStockIsLowerThanCartQuantity() {
        Long productId = 1001L;
        Integer newStock = 2;

        StockNotificationResponse notification = new StockNotificationResponse(productId, newStock);

        UUID userId = UUID.randomUUID();
        CartDetail cartDetail = mock(CartDetail.class);
        when(cartDetail.getProductId()).thenReturn(productId);
        when(cartDetail.getQuantity()).thenReturn(5);

        Cart cart = mock(Cart.class);
        when(cart.getUserId()).thenReturn(userId);
        when(cart.getCartDetails()).thenReturn(List.of(cartDetail));

        when(cartServices.getAllActiveCarts()).thenReturn(List.of(cart));

        consumer.receiveStockChangeNotification(notification);

        ArgumentCaptor<UserProductStockChangedNotificationDTO> captor = ArgumentCaptor.forClass(UserProductStockChangedNotificationDTO.class);

        verify(rabbitTemplate).convertAndSend(eq("cart"), eq("product.changed"), captor.capture());

        UserProductStockChangedNotificationDTO sentNotification = captor.getValue();
        assertEquals(userId, sentNotification.userId());
        assertEquals(productId, sentNotification.productId());
    }

    @Test
    void shouldNotSendNotificationIfStockIsSufficient() {
        Long productId = 1001L;
        Integer newStock = 10;

        StockNotificationResponse notification = new StockNotificationResponse(productId, newStock);

        UUID userId = UUID.randomUUID();

        CartDetail cartDetail = new CartDetail();
        cartDetail.setProductId(productId);
        cartDetail.setQuantity(3); // quantity < stock

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCartDetails(List.of(cartDetail));

        when(cartServices.getAllActiveCarts()).thenReturn(List.of(cart));

        consumer.receiveStockChangeNotification(notification);

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), (Object) any());
    }

    @Test
    void shouldNotSendNotificationIfProductIdDoesNotMatch() {
        Long notificationProductId = 1001L;
        Long otherProductId = 2002L;

        StockNotificationResponse notification = new StockNotificationResponse(notificationProductId, 5);

        UUID userId = UUID.randomUUID();
        CartDetail cartDetail = mock(CartDetail.class);
        when(cartDetail.getProductId()).thenReturn(otherProductId); // not the same

        Cart cart = mock(Cart.class);
        when(cart.getCartDetails()).thenReturn(List.of(cartDetail));

        when(cartServices.getAllActiveCarts()).thenReturn(List.of(cart));

        consumer.receiveStockChangeNotification(notification);

        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), (Object) any());
    }
}