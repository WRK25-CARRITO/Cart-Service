package com.gft.wrk2025carrito.shopping_cart.infrastructure.messages;

import com.gft.wrk2025carrito.shopping_cart.application.service.CartServicesImpl;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.dto.StockNotificationResponse;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.dto.UserProductStockChangedNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMQProductStockChangedConsumer {
    private final RabbitTemplate rabbitTemplate;
    Logger logger = LoggerFactory.getLogger(RabbitMQProductStockChangedConsumer.class);
    private final CartServicesImpl cartServices;

    public RabbitMQProductStockChangedConsumer(final CartServicesImpl cartServices, RabbitTemplate rabbitTemplate) {
        this.cartServices = cartServices;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "products.cart.stock.changed")
    public void receiveStockChangeNotification(StockNotificationResponse stockNotificationResponse){
        logger.info("Received notification from coms saying Stock of product is now[{}]: {}", stockNotificationResponse);

        List<Cart> activeCarts = cartServices.getAllActiveCarts();

        for(Cart cart : activeCarts){

            for(CartDetail cartDetail : cart.getCartDetails()){
                if(cartDetail.getProductId().equals( stockNotificationResponse.productId() ) && cartDetail.getQuantity() > stockNotificationResponse.stock()){

                    UserProductStockChangedNotificationDTO userNotification =  new UserProductStockChangedNotificationDTO(cart.getUserId(), stockNotificationResponse.productId());
                    final String exchange = "cart";
                    final String routingKey = "product.changed";

                    rabbitTemplate.convertAndSend(exchange, routingKey, userNotification);
                }
            }
        }
    }
}
