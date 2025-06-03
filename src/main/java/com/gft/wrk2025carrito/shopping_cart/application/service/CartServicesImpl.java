package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartDTO;
import com.gft.wrk2025carrito.shopping_cart.application.dto.ProductDTO;
import com.gft.wrk2025carrito.shopping_cart.application.helper.CartCalculator;
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

    private static final String PRODUCTS_URL = "https://workshop-7uvd.onrender.com/api/v1/products";
    private static final String PROMOTIONS_URL = "";

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
                .map(entry -> CartDetail.build(entry.getKey(), entry.getValue(), BigDecimal.ZERO, 0.0))
                .toList();

        cart.setCartDetails(newCartDetails);
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

        // Request products
        ResponseEntity<ProductDTO[]> resp = restTemplate.getForEntity(PRODUCTS_URL, ProductDTO[].class);
        ProductDTO[] allProductsArray = resp.getBody();
        if (allProductsArray == null) {
            throw new IllegalArgumentException("Failed to fetch products to compute total");
        }

        List<ProductDTO> allProducts = Arrays.asList(allProductsArray);

        Map<Long, Integer> quantities = cart.getCartDetails().stream()
                .collect(Collectors.toMap(
                        CartDetail::getProductId,
                        CartDetail::getQuantity,
                        Integer::sum
                ));
        Set<Long> cartProductsIds = quantities.keySet();

        // matching products
        List<ProductDTO> inCartProducts = allProducts.stream()
                .filter(requestProduct -> cartProductsIds.contains(requestProduct.id()))
                .toList();

        // matching products equal size as cart products
        if (inCartProducts.size() != cartProductsIds.size()) {
            throw new IllegalArgumentException("One or more product IDs in the cart are invalid");
        }

        // calcular el precio y peso de cada cartDetail
        List<CartDetail> cartDetailsList = quantities.entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    int quantity = entry.getValue();

                    // Encontrar el ProductDTO concreto
                    ProductDTO productDTO = inCartProducts.stream()
                            .filter(p -> p.id().equals(productId))
                            .findFirst()
                            .orElseThrow(); // Ya no puede fallar porque comprobamos tamaños arriba

                    BigDecimal unitPrice = productDTO.price();
                    double unitWeight  = productDTO.weight();

                    // Calcular totales por línea
                    BigDecimal totalPriceDetalle = unitPrice.multiply(BigDecimal.valueOf(quantity));
                    double totalWeightDetalle = unitWeight * quantity;

                    return CartDetail.build(productId, quantity, totalPriceDetalle, totalWeightDetalle);
                })
                .toList();

        // Calculate total price
        BigDecimal totalPrice = cartDetailsList.stream()
                .map(CartDetail::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double totalWeight = cartDetailsList.stream()
                .mapToDouble(CartDetail::getTotalWeight)
                .sum();

        BigDecimal shippingCost = cartCalculator.calculateShippingCost(totalWeight);
        totalPrice = totalPrice.add(shippingCost);

        // OBTENER PROMOCIONES
        //
        // ResponseEntity<Long[]> promoResp = restTemplate.getForEntity(PROMOTIONS_URL, Long[].class);
        // Long[] promoArray = promoResp.getBody();
        // List<Long> promotionsIds = List.of(promoArray);
        List<Long> promotionsIds = Collections.emptyList();

        cart.setCartDetails(cartDetailsList);
        cart.setTotalPrice(totalPrice);
        cart.setTotalWeight(totalWeight);
        cart.setPromotionIds(promotionsIds);
        cart.setState(CartState.PENDING);

        cartRepository.save(cartFactory.toEntity(cart));
        return cart;
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

        // recuperar todos los productos para recalcular totales
        ResponseEntity<ProductDTO[]> resp = restTemplate.getForEntity(PRODUCTS_URL, ProductDTO[].class);
        ProductDTO[] allProductsArray = resp.getBody();
        if (allProductsArray == null) {
            throw new IllegalArgumentException("Failed to fetch products to compute total");
        }
        List<ProductDTO> allProducts = Arrays.asList(allProductsArray);

        Map<Long, Integer> quantities = cart.getCartDetails().stream()
                .collect(Collectors.toMap(
                        CartDetail::getProductId,
                        CartDetail::getQuantity,
                        Integer::sum
                ));
        Set<Long> cartProductsIds = quantities.keySet();

        // Filtramos sólo los que están en el carrito
        List<ProductDTO> inCartProducts = allProducts.stream()
                .filter(requestProduct -> cartProductsIds.contains(requestProduct.id()))
                .toList();

        if (inCartProducts.size() != cartProductsIds.size()) {
            throw new IllegalArgumentException("One or more product IDs in the cart are invalid");
        }

        // calcular el precio y peso de cada cartDetail
        List<CartDetail> cartDetailsList = quantities.entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    int quantity = entry.getValue();

                    ProductDTO productDTO = inCartProducts.stream()
                            .filter(p -> p.id().equals(productId))
                            .findFirst()
                            .orElseThrow();

                    BigDecimal unitPrice = productDTO.price();
                    double unitWeight  = productDTO.weight();

                    BigDecimal totalPriceCartDetail = unitPrice.multiply(BigDecimal.valueOf(quantity));
                    double totalWeightCartDetail = unitWeight * quantity;

                    return CartDetail.build(productId, quantity, totalPriceCartDetail, totalWeightCartDetail);
                })
                .toList();

        // calcular precio del carrito
        BigDecimal rawPrice = cartDetailsList.stream()
                .map(CartDetail::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double totalWeight = cartDetailsList.stream()
                .mapToDouble(CartDetail::getTotalWeight)
                .sum();

        BigDecimal shippingCost = cartCalculator.calculateShippingCost(totalWeight);
        BigDecimal pricePlusShipping = rawPrice.add(shippingCost);

        // Aplicar impuestos y cargos
        BigDecimal priceAfterTax;
        BigDecimal priceAfterCharge;
        try {
            priceAfterTax    = cartCalculator.applyTax(pricePlusShipping, newCountryTax.getTax());
            priceAfterCharge = cartCalculator.applyCharge(priceAfterTax, newPaymentMethod.getCharge());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error applying tax/charge: " + e.getMessage(), e);
        }

        cart.setTotalPrice(priceAfterCharge);
        cart.setTotalWeight(totalWeight);
        cart.setCountryTax(newCountryTax);
        cart.setPaymentMethod(newPaymentMethod);
        cart.setState(CartState.CLOSED);
        cart.setUpdatedAt(new Date());

        cartRepository.save(cartFactory.toEntity(cart));
        return cart;
    }

}
