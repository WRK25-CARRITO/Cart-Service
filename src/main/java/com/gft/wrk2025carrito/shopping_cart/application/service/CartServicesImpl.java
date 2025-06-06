package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderLineDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderOffers;
import com.gft.wrk2025carrito.shopping_cart.application.helper.CartCalculator;
import com.gft.wrk2025carrito.shopping_cart.application.service.client.OrderMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.domain.repository.CartRepository;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServicesImpl implements CartServices {

    private final CartRepository cartRepository;
    private final CartFactory cartFactory;
    private final RestTemplate restTemplate;
    private final CartCalculator cartCalculator;
    private final OrderMicroserviceService orderMicroserviceService;


    @Override
    @Transactional
    public List<Cart> getAll(){
        return cartRepository.findAll();
    }

    @Override
    @Transactional
    public Cart getById(UUID id) {
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if(!cartRepository.existsById(id)) {
            throw new IllegalArgumentException("Cart with id " + id + " does not exist");
        }

        return cartRepository.findById(id);

    }

    @Override
    public Cart showTotalPriceAndWeight(UUID id) throws Exception {
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if(!cartRepository.existsById(id)) {
            throw new IllegalArgumentException("Cart with id " + id + " does not exist");
        }

        Cart cart = cartRepository.findById(id);

        return cartCalculator.calculateAndUpdateCart(cart);
    }

    @Override
    @Transactional
    public Cart updateState(UUID cartId, CartDTO cartDTO) {

        if (cartId == null) {
            throw new IllegalArgumentException("Cart id cannot be null");
        }
        if (!cartRepository.existsById(cartId)) {
            throw new IllegalArgumentException("Cart with id " + cartId + " does not exist");
        }
        if (cartDTO == null) {
            throw new IllegalArgumentException("New cart cannot be null");
        }

        Cart cart = cartRepository.findById(cartId);
        CartState currentState = cart.getState();
        CartState targetState = cartDTO.cartState();

        // Solo se permiten transiciones desde ACTIVE o desde PENDING
        if (currentState != CartState.ACTIVE &&
                currentState != CartState.PENDING) {
            throw new IllegalArgumentException(
                    "Cart with id " + cartId + " cannot be updated from state " + currentState
            );
        }

        if (targetState == CartState.PENDING) {
            return handlePending(cart);
        }

        if (targetState == CartState.CLOSED) {
            return handleClosed(cart, cartDTO);
        }

        throw new IllegalArgumentException("Cannot update cart state to " + targetState);
    }

    @Override
    @Transactional
    public void update(UUID cartId, Map<Long, Integer> cartProducts) {
        String url = "https://workshop-7uvd.onrender.com/api/v1/products/list-by-ids";

        if (cartId == null) {
            throw new IllegalArgumentException("Cart id cannot be null");
        }

        if (!cartRepository.existsById(cartId)) {
            throw new IllegalArgumentException("Cart with id " + cartId + " does not exist");
        }

        Cart cart = cartRepository.findById(cartId);
        if (cart.getState() == CartState.PENDING || cart.getState() == CartState.CLOSED) {
            throw new IllegalArgumentException("Cart with id " + cartId + " cannot be updated");
        }

        // Eliminar duplicados y sumar cantidades
        Map<Long, Integer> productQuantities = cartProducts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));

        List<Long> productIds = new ArrayList<>(productQuantities.keySet());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Long>> request = new HttpEntity<>(productIds, headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Object[].class
        );

        Object[] productos = response.getBody();
        if (productos == null || productos.length != productIds.size()) {
            throw new IllegalArgumentException("One or more product IDs are invalid");
        }

        List<CartDetail> newCartDetails = productQuantities.entrySet().stream()
                .map(entry -> CartDetail.build(entry.getKey(), entry.getValue(),BigDecimal.ZERO, 0.0))
                .toList();

        cart.setCartDetails(newCartDetails);
        cart.setUpdatedAt(new Date());

        cartRepository.save(cartFactory.toEntity(cart));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) throw new IllegalArgumentException("Cart ID must not be null");

        if (!cartRepository.existsById(id)) throw new IllegalStateException("No cart found with ID " + id);

        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("User ID must not be null");

        List<Cart> carts = cartRepository.findByUserId(userId);

        if (carts.isEmpty()) throw new IllegalStateException("No carts found for user with ID: " + userId);

        cartRepository.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    public Cart createCart(UUID userId) {

        if(userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        boolean exists = cartRepository.cartExistsByUserIdAndStateActive(userId);

        if (exists){
            throw new IllegalArgumentException(" An active cart already exists for user");
        }

        Cart cart = Cart.build(new CartId(), userId, null, null, null, null, new Date(), null, java.util.Collections.emptyList(), CartState.ACTIVE, java.util.Collections.emptyList());

        return cartRepository.create(cart);
    }

    @Override
    @Transactional
    public UUID sendCartToOrder(UUID id) throws Exception {

        if (id == null) throw new IllegalArgumentException("Cart ID must not be null");

        if (!cartRepository.existsById(id)) throw new IllegalArgumentException("No cart found with ID " + id);

        Cart cart = cartRepository.findById(id);

        if (cart.getState() != CartState.CLOSED) {
            throw new IllegalArgumentException("Cannot send cart state from " + cart.getState() + " to Orders");
        }

        Cart calculatedCart = cartCalculator.calculateAndUpdateCart(cart);

        OrderDTO order = createOrderFromCart(calculatedCart);

        UUID generatedUuid = orderMicroserviceService.sendAOrder(order);

        createCart(cart.getUserId());

        return generatedUuid;
    }

    private Cart handlePending(Cart cart) {

        if (cart.getState() != CartState.ACTIVE) {
            throw new IllegalArgumentException(
                    "Cannot update cart state from " + cart.getState() + " to PENDING"
            );
        }

        if (cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
            throw new IllegalArgumentException(
                    "There must be at least one product to change state to PENDING"
            );
        }

        Cart updatedCart;
        try {
            updatedCart = cartCalculator.calculateAndUpdateCart(cart);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error calculating pending totals: " + e.getMessage(), e);
        }

        updatedCart.setState(CartState.PENDING);

        updatedCart.setPromotionIds(orderMicroserviceService.getAllOrderPromotions(updatedCart));
        updatedCart.setUpdatedAt(new Date());

        cartRepository.save(cartFactory.toEntity(updatedCart));

        return updatedCart;
    }

    private Cart handleClosed(Cart cart, CartDTO cartDTO) {

        if (cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
            throw new IllegalArgumentException(
                    "There must be at least one product in the cart to change state to CLOSED"
            );
        }

        if (cart.getState() != CartState.PENDING) {
            throw new IllegalArgumentException(
                    "Cannot update cart state from " + cart.getState() + " to CLOSED"
            );
        }

        CountryTax newCountryTax = cartDTO.countryTax();
        PaymentMethod newPaymentMethod = cartDTO.paymentMethod();
        if (newCountryTax == null || newPaymentMethod == null) {
            throw new IllegalArgumentException(
                    "Country tax and payment method cannot be null when updating to CLOSED"
            );
        }

        cart.setCountryTax(newCountryTax);
        cart.setPaymentMethod(newPaymentMethod);

        Cart updatedCart;
        try {
            updatedCart = cartCalculator.calculateAndUpdateCart(cart);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error calculating closed totals: " + e.getMessage(), e);
        }

        updatedCart.setState(CartState.CLOSED);

        updatedCart.setUpdatedAt(new Date());
        cartRepository.save(cartFactory.toEntity(updatedCart));
        return updatedCart;
    }

    private OrderDTO createOrderFromCart(Cart cart){

        UUID orderId = cart.getId().id();

        List<OrderOffers> orderOffers = fromPromotionIds(cart.getPromotionIds(), orderId);

        OrderDTO order = OrderDTO.builder()
                .id(orderId)
                .userId(cart.getUserId())
                .creationDate(LocalDateTime.now())
                .totalPrice(cart.getTotalPrice())
                .countryTax(cart.getCountryTax().getTax())
                .paymentMethod(cart.getPaymentMethod().getCharge())
                .ordersOffers(orderOffers)
                .orderReturn(false)
                .build();

        List<OrderLineDTO> orderLines = OrderLineDTO.fromCartDetailList(cart.getCartDetails());
        order.setOrderLines(orderLines);

        return order;
    }

    public static List<OrderOffers> fromPromotionIds(List<Long> promotionIds, UUID orderId) {
        if (promotionIds == null) {
            return List.of();
        }

        return promotionIds.stream()
                .map(promoId -> {
                    OrderOffers oo = new OrderOffers();
                    oo.setOrderId(orderId);
                    oo.setOfferId(promoId);
                    return oo;
                })
                .collect(Collectors.toList());
    }

}
