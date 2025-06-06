package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.OrderLineDTO;
import com.gft.wrk2025carrito.shopping_cart.application.helper.CartCalculator;
import com.gft.wrk2025carrito.shopping_cart.application.service.client.OrderMicroserviceService;
import com.gft.wrk2025carrito.shopping_cart.application.helper.CartCalculator;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartId;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.CartState;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cartDetail.CartDetail;
import com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax;
import com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartEntityRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServicesImplTest {

    @Mock
    private CartEntityRepositoryImpl repository;

    @Mock
    private CartFactory cartFactory;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CartCalculator cartCalculator;

    @Mock
    private OrderMicroserviceService orderMicroserviceService;

    @InjectMocks
    private CartServicesImpl cartService;

    private UUID sharedCartId;
    private Cart baseCart;
//    private CartDTO dtoPending;
//    private CartDTO dtoClosed;
    private Cart calculatedCart;
    private Cart mockPendingCart;
    private Cart mockClosedCart;
    private CountryTax countryTaxMock;
    private PaymentMethod paymentMethodMock;


    @BeforeEach
    void setUp() {
        sharedCartId = UUID.randomUUID();

        calculatedCart = mock(Cart.class);
        baseCart = mock(Cart.class);

//        dtoPending = new CartDTO(CartState.PENDING, null, null);

        countryTaxMock      = mock(CountryTax.class);
        paymentMethodMock   = mock(PaymentMethod.class);
//        dtoClosed = new CartDTO(CartState.CLOSED, countryTaxMock, paymentMethodMock);

        mockPendingCart = mock(Cart.class);
        lenient().when(mockPendingCart.getState()).thenReturn(CartState.PENDING);

        mockClosedCart = mock(Cart.class);
        lenient().when(mockClosedCart.getState()).thenReturn(CartState.CLOSED);

        lenient().when(repository.existsById(sharedCartId)).thenReturn(true);
        lenient().when(repository.findById(sharedCartId)).thenReturn(baseCart);
    }

    @Test
    void getAllCarts_ok() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Cart> result = cartService.getAll();

        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    void getCartById_ok() {
        Cart mockCart = mock(Cart.class);

        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(mockCart);

        Cart cart = cartService.getById(sharedCartId);

        assertNotNull(cart);
        assertEquals(mockCart, cart);
    }

    @Test
    void getCartById_isNull() {
        assertThrows(IllegalArgumentException.class, () -> cartService.getById(null));
    }

    @Test
    void getCartById_notFound() {
        when(repository.existsById(sharedCartId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> cartService.getById(sharedCartId));
    }

    @Test
    void should_throwException_whenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> cartService.delete(null));
    }

    @Test
    void should_throwException_whenCartNotExists() {
        when(repository.existsById(sharedCartId)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> cartService.delete(sharedCartId));
    }

    @Test
    void should_deleteCart_whenExists() {
        when(repository.existsById(sharedCartId)).thenReturn(true);

        cartService.delete(sharedCartId);

        verify(repository, times(1)).deleteById(sharedCartId);
    }

    @Test
    void should_throwException_whenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> cartService.deleteAllByUserId(null));
    }

    @Test
    void should_throwException_whenCartByUser_notExists() {
        when(repository.findByUserId(sharedCartId)).thenReturn(Collections.emptyList());

        assertThrows(IllegalStateException.class, () -> cartService.deleteAllByUserId(sharedCartId));
    }

    @Test
    void should_deleteCartByUser_whenExists() {
        List<Cart> carts = List.of(mock(Cart.class));
        when(repository.findByUserId(sharedCartId)).thenReturn(carts);

        cartService.deleteAllByUserId(sharedCartId);

        verify(repository, times(1)).deleteAllByUserId(sharedCartId);
    }

    @Test
    void should_updateCart_whenExists() {
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        Cart existingCart = mock(Cart.class);

        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(existingCart);
        when(repository.save(any())).thenReturn(existingCart);

        Object[] fakeProductResponse = new Object[]{ new Object() };
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object[].class))
        ).thenReturn(new ResponseEntity<>(fakeProductResponse, HttpStatus.OK));

        cartService.update(sharedCartId, productData);

        verify(existingCart).setCartDetails(any());
        verify(repository).save(any());
    }

    @Test
    void should_throwException_whenCartStateIsPendingOrClosed() {
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        Cart cart = mock(Cart.class);
        when(cart.getState()).thenReturn(CartState.PENDING);
        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(cart);

        assertThrows(IllegalArgumentException.class, () -> cartService.update(sharedCartId, productData));
    }

    @Test
    void should_throwException_whenProductIdsMismatch() {
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        Cart cart = mock(Cart.class);
        when(cart.getState()).thenReturn(CartState.ACTIVE);
        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(cart);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object[].class)))
                .thenReturn(new ResponseEntity<>(new Object[0], HttpStatus.OK));

        assertThrows(IllegalArgumentException.class, () -> cartService.update(sharedCartId, productData));
    }

    @Test
    void should_throwException_whenProductResponseIsNull() {
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        Cart cart = mock(Cart.class);
        when(cart.getState()).thenReturn(CartState.ACTIVE);
        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(cart);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object[].class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        assertThrows(IllegalArgumentException.class, () -> cartService.update(sharedCartId, productData));
    }

    @Test
    void should_throwException_whenCartStateIsClosed() {
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        Cart cart = mock(Cart.class);
        when(cart.getState()).thenReturn(CartState.CLOSED);
        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(cart);

        assertThrows(IllegalArgumentException.class, () -> cartService.update(sharedCartId, productData));
    }

    @Test
    void should_throwException_onUpdate_whenCartIdIsNull() {
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        assertThrows(IllegalArgumentException.class, () -> cartService.update(null, productData));
        verifyNoInteractions(repository);
    }

    @Test
    void should_throwException_onUpdate_whenCart_doesNotExist() {
        Long productId = 1L;
        Map<Long, Integer> productData = Map.of(productId, 3);

        when(repository.existsById(sharedCartId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> cartService.update(sharedCartId, productData));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.createCart(null);
        });
        assertEquals("User ID must not be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfActiveCartExists() {
        UUID userId = UUID.randomUUID();
        when(repository.cartExistsByUserIdAndStateActive(userId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.createCart(userId);
        });
        assertEquals(" An active cart already exists for user", exception.getMessage());
    }

    @Test
    void shouldCreateCartIfNoneExists() {
        UUID userId = UUID.randomUUID();
        when(repository.cartExistsByUserIdAndStateActive(userId)).thenReturn(false);

        Cart expectedCart = Cart.build(
                new CartId(), userId, null, null, null, null,
                new Date(), null,
                java.util.Collections.emptyList(),
                CartState.ACTIVE,
                java.util.Collections.emptyList()
        );

        when(repository.create(any(Cart.class))).thenReturn(expectedCart);

        Cart result = cartService.createCart(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(CartState.ACTIVE, result.getState());
        verify(repository).create(any(Cart.class));
    }

    @Test
    void updateState_ThrowsWhenCartIdIsNull() {
        CartDTO dto = new CartDTO(CartState.PENDING, null, null);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(null, dto)
        );
        assertEquals("Cart id cannot be null", ex.getMessage());
    }

    @Test
    void updateState_ThrowsWhenCartDoesNotExist() {
        UUID fakeId = UUID.randomUUID();
        when(repository.existsById(fakeId)).thenReturn(false);

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(fakeId, dto)
        );
        assertEquals("Cart with id " + fakeId + " does not exist", ex.getMessage());
    }

    @Test
    void updateState_ThrowsWhenCartDTONull() {
        UUID someId = UUID.randomUUID();
        when(repository.existsById(someId)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(someId, null)
        );
        assertEquals("New cart cannot be null", ex.getMessage());
    }

    @Test
    void updateState_ThrowsWhenCurrentStateNotActiveOrPending() {
        UUID cartId = UUID.randomUUID();
        Cart fakeCart = mock(Cart.class);
        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(fakeCart);
        when(fakeCart.getState()).thenReturn(CartState.CLOSED);

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );
        assertEquals(
                "Cart with id " + cartId + " cannot be updated from state CLOSED",
                ex.getMessage()
        );
    }

    @Test
    void updateState_ThrowsWhenTargetStateInvalid() {
        UUID cartId = UUID.randomUUID();
        Cart fakeCart = mock(Cart.class);

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(fakeCart);
        when(fakeCart.getState()).thenReturn(CartState.ACTIVE);

        CartDTO dto = new CartDTO(CartState.ACTIVE, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );
        assertEquals("Cannot update cart state to ACTIVE", ex.getMessage());
    }

    @Test
    void updateState_FromActiveToPending_InvokesHandlePendingAndReturnsThatCart() throws Exception {
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.ACTIVE);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);


        Cart carritoPendiente = mock(Cart.class);
        when(carritoPendiente.getState()).thenReturn(CartState.PENDING);

        when(cartCalculator.calculateAndUpdateCart(eq(cartInicial)))
                .thenReturn(carritoPendiente);

        Cart resultado = cartService.updateState(sharedCartId, dto);

        assertEquals(carritoPendiente, resultado);
        assertEquals(CartState.PENDING, resultado.getState());

        verify(cartCalculator, times(1)).calculateAndUpdateCart(eq(cartInicial));
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateState_FromPendingToClosed_InvokesHandleClosedAndReturnsThatCart() throws Exception {
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(cartInicial);

        var countryTaxMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax.class);
        var paymentMethodMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod.class);

        CartDTO dto = new CartDTO(CartState.CLOSED, countryTaxMock, paymentMethodMock);

        Cart carritoCerrado = mock(Cart.class);
        when(carritoCerrado.getState()).thenReturn(CartState.CLOSED);

        when(carritoCerrado.getCountryTax()).thenReturn(countryTaxMock);
        when(carritoCerrado.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(cartCalculator.calculateAndUpdateCart(eq(cartInicial)))
                .thenReturn(carritoCerrado);

        Cart resultado = cartService.updateState(sharedCartId, dto);

        assertEquals(carritoCerrado, resultado);
        assertEquals(CartState.CLOSED, resultado.getState());

        assertEquals(countryTaxMock, resultado.getCountryTax());
        assertEquals(paymentMethodMock, resultado.getPaymentMethod());

        verify(cartCalculator, times(1)).calculateAndUpdateCart(eq(cartInicial));
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateState_FromPendingToPending_ThrowsCannotUpdateToPending() {
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);

        when(repository.existsById(sharedCartId)).thenReturn(true);
        when(repository.findById(sharedCartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(sharedCartId, dto)
        );

        assertEquals(
                "Cannot update cart state from PENDING to PENDING",
                ex.getMessage()
        );
    }

    @Test
    void updateState_ActiveToPending_NoCartDetails_ThrowsMustHaveAtLeastOneProduct() {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.ACTIVE);
        when(cartInicial.getCartDetails()).thenReturn(Collections.emptyList());

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertEquals(
                "There must be at least one product to change state to PENDING",
                ex.getMessage()
        );
    }

    @Test
    void updateState_ActiveToClosed_ThrowsCannotUpdateToClosed() {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.ACTIVE);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.CLOSED, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertEquals(
                "Cannot update cart state from ACTIVE to CLOSED",
                ex.getMessage()
        );
    }

    @Test
    void updateState_PendingToClosed_NoCartDetails_ThrowsMustHaveAtLeastOneProductForClosed() {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);
        when(cartInicial.getCartDetails()).thenReturn(Collections.emptyList());

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.CLOSED, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertEquals(
                "There must be at least one product in the cart to change state to CLOSED",
                ex.getMessage()
        );
    }

    @Test
    void updateState_PendingToClosed_NullTaxOrPayment_ThrowsCountryTaxAndPaymentCannotBeNull() {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.CLOSED, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertEquals(
                "Country tax and payment method cannot be null when updating to CLOSED",
                ex.getMessage()
        );
    }

    @Test
    void updateState_PendingToClosed_StateNotPending_ThrowsCannotUpdateToClosed() {

        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.ACTIVE);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.CLOSED, mock(com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax.class),
                mock(com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod.class));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertEquals(
                "Cannot update cart state from ACTIVE to CLOSED",
                ex.getMessage()
        );
    }

    @Test
    void updateState_FromActiveToPending_CalculatorThrows_PropagatesErrorPending() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.ACTIVE);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        when(cartCalculator.calculateAndUpdateCart(eq(cartInicial)))
                .thenThrow(new RuntimeException("algo falló internamente"));

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertTrue(
                ex.getMessage().contains("Error calculating pending totals: algo falló internamente"),
                "Debería lanzar IllegalArgumentException con prefijo 'Error calculating pending totals:'"
        );

        verify(cartCalculator, times(1)).calculateAndUpdateCart(eq(cartInicial));
        verify(repository, never()).save(any());
    }

    @Test
    void updateState_ActiveToPending_NullCartDetails_ThrowsMustHaveAtLeastOneProduct() {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.ACTIVE);
        when(cartInicial.getCartDetails()).thenReturn(null);

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        CartDTO dto = new CartDTO(CartState.PENDING, null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertEquals(
                "There must be at least one product to change state to PENDING",
                ex.getMessage()
        );
    }

    @Test
    void updateState_FromPendingToClosed_CalculatorThrows_PropagatesErrorClosed() throws Exception {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        var countryTaxMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax.class);
        var paymentMethodMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod.class);

        when(cartCalculator.calculateAndUpdateCart(eq(cartInicial)))
                .thenThrow(new RuntimeException("error interno cerrado"));

        CartDTO dto = new CartDTO(CartState.CLOSED, countryTaxMock, paymentMethodMock);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertTrue(
                ex.getMessage().contains("Error calculating closed totals: error interno cerrado"),
                "Debería lanzar IllegalArgumentException con prefijo 'Error calculating closed totals:'"
        );

        verify(cartCalculator, times(1)).calculateAndUpdateCart(eq(cartInicial));
        verify(repository, never()).save(any());
    }

    @Test
    void updateState_PendingToClosed_NullCartDetails_ThrowsMustHaveAtLeastOneProductForClosed() {
        UUID cartId = UUID.randomUUID();
        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);
        when(cartInicial.getCartDetails()).thenReturn(null);

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        var countryTaxMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax.class);
        var paymentMethodMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod.class);
        CartDTO dto = new CartDTO(CartState.CLOSED, countryTaxMock, paymentMethodMock);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dto)
        );

        assertEquals(
                "There must be at least one product in the cart to change state to CLOSED",
                ex.getMessage()
        );
    }

    @Test
    void updateState_PendingToClosed_PaymentMethodNull_ThrowsCountryTaxAndPaymentCannotBeNull() throws Exception {
        UUID cartId = UUID.randomUUID();

        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        var countryTaxMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.countryTax.CountryTax.class);
        CartDTO dtoConTaxPeroSinPago = new CartDTO(CartState.CLOSED, countryTaxMock, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dtoConTaxPeroSinPago)
        );

        assertEquals(
                "Country tax and payment method cannot be null when updating to CLOSED",
                ex.getMessage()
        );

        verify(cartCalculator, never()).calculateAndUpdateCart(any());
        verify(repository, never()).save(any());
    }


    @Test
    void updateState_PendingToClosed_CountryTaxNull_ThrowsCountryTaxAndPaymentCannotBeNull() throws Exception {
        UUID cartId = UUID.randomUUID();

        Cart cartInicial = mock(Cart.class);
        when(cartInicial.getState()).thenReturn(CartState.PENDING);
        when(cartInicial.getCartDetails()).thenReturn(List.of(mock(CartDetail.class)));

        when(repository.existsById(cartId)).thenReturn(true);
        when(repository.findById(cartId)).thenReturn(cartInicial);

        var paymentMethodMock = mock(com.gft.wrk2025carrito.shopping_cart.domain.model.paymentMethod.PaymentMethod.class);
        CartDTO dtoSinTaxPeroConPago = new CartDTO(CartState.CLOSED, null, paymentMethodMock);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.updateState(cartId, dtoSinTaxPeroConPago)
        );

        assertEquals(
                "Country tax and payment method cannot be null when updating to CLOSED",
                ex.getMessage()
        );

        verify(cartCalculator, never()).calculateAndUpdateCart(any());
        verify(repository, never()).save(any());
    }

    @Test
    void showTotalPriceAndWeight_nullId_throwsIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.showTotalPriceAndWeight(null)
        );
        assertEquals("Id cannot be null", exception.getMessage());

        verifyNoInteractions(repository);
        verifyNoInteractions(cartCalculator);
    }

    @Test
    void showTotalPriceAndWeight_cartNotFound_throwsIllegalArgumentException() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.showTotalPriceAndWeight(id)
        );
        assertEquals("Cart with id " + id + " does not exist", exception.getMessage());

        verify(repository, times(1)).existsById(id);
        verify(repository, never()).findById(any(UUID.class));
        verifyNoInteractions(cartCalculator);
    }

    @Test
    void showTotalPriceAndWeight_success_returnsCalculatedCart() throws Exception {
        CartId  cartId = new CartId();
        UUID id = cartId.id();

        Cart mockCart = mock(Cart.class);

        Cart calculatedCart = mock(Cart.class);
        when(calculatedCart.getId()).thenReturn(cartId);
        when(calculatedCart.getState()).thenReturn(CartState.ACTIVE);
        when(calculatedCart.getTotalPrice()).thenReturn(BigDecimal.valueOf(123.45));
        when(calculatedCart.getTotalWeight()).thenReturn(67.89);

        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(mockCart);
        when(cartCalculator.calculateAndUpdateCart(mockCart)).thenReturn(calculatedCart);

        Cart result = cartService.showTotalPriceAndWeight(id);

        assertNotNull(result);
        assertEquals(cartId,          result.getId());
        assertEquals(CartState.ACTIVE, result.getState());
        assertNotNull(result.getTotalPrice());
        assertNotNull(result.getTotalWeight());

        verify(repository,       times(1)).existsById(id);
        verify(repository,       times(1)).findById(id);
        verify(cartCalculator,   times(1)).calculateAndUpdateCart(mockCart);
    }

    @Test
    void sendCartToOrder_nullId_throwsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.sendCartToOrder(null)
        );
        assertEquals("Cart ID must not be null", ex.getMessage());
        verifyNoInteractions(repository, cartCalculator, orderMicroserviceService);
    }

    @Test
    void sendCartToOrder_cartNotFound_throwsIllegalArgumentException() {
        UUID notFoundId = UUID.randomUUID();
        when(repository.existsById(notFoundId)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.sendCartToOrder(notFoundId)
        );
        assertEquals("No cart found with ID " + notFoundId, ex.getMessage());
        verify(repository).existsById(notFoundId);
        verify(repository, never()).findById(any());
        verifyNoInteractions(cartCalculator, orderMicroserviceService);
    }

    @Test
    void sendCartToOrder_stateNotClosed_throwsIllegalArgumentException() {
        // baseCart.getState() devuelve ACTIVE
        when(baseCart.getState()).thenReturn(CartState.ACTIVE);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.sendCartToOrder(sharedCartId)
        );
        assertEquals(
                "Cannot send cart state from ACTIVE to Orders",
                ex.getMessage()
        );
        verify(repository).existsById(sharedCartId);
        verify(repository).findById(sharedCartId);
        verifyNoInteractions(cartCalculator, orderMicroserviceService);
    }

    @Test
    void should_sendCart_success() throws Exception {

        Cart initialCart = entireCart();

        UUID orderSendUuid = UUID.randomUUID();

        Cart calculatedCart = entireCart();
        initialCart.setTotalPrice(BigDecimal.valueOf(50));
        initialCart.setTotalWeight(0.5);

        when(repository.existsById(initialCart.getId().id())).thenReturn(true);
        when(repository.findById(initialCart.getId().id())).thenReturn(initialCart);
        when(cartCalculator.calculateAndUpdateCart(initialCart)).thenReturn(calculatedCart);

        when(orderMicroserviceService.sendAOrder(any(OrderDTO.class))).thenReturn(orderSendUuid);

        UUID resultado = cartService.sendCartToOrder(initialCart.getId().id());

        assertEquals(orderSendUuid, resultado);

        verify(repository).existsById(initialCart.getId().id());
        verify(repository).findById(initialCart.getId().id());
        verify(cartCalculator).calculateAndUpdateCart(initialCart);
        verify(orderMicroserviceService).sendAOrder(argThat(order ->
                order.getOrderLines() != null && !order.getOrderLines().isEmpty()
        ));

    }


    private Cart entireCart(){
        PaymentMethod method = new PaymentMethod();
        method.setCharge(0.02);
        CountryTax countryTax = new CountryTax();
        countryTax.setTax(0.2);

        CartDetail detail = new CartDetail();
        detail.setProductId(1L);
        detail.setQuantity(1);
        detail.setTotalWeight(0.5);
        detail.setTotalPrice(new BigDecimal(25));

        Cart cart = new Cart();
        cart.setId(new CartId());
        cart.setUserId(UUID.randomUUID());
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new  Date());
        cart.setState(CartState.CLOSED);
        cart.setCountryTax(countryTax);
        cart.setPaymentMethod(method);
        cart.setCartDetails(List.of(detail));
        cart.setPromotionIds(List.of());

        return cart;
    }


}