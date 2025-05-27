package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartServices cartServices;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        cartServices.delete(id);
    }

    @DeleteMapping("/user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUser(@PathVariable UUID id) {
        cartServices.deleteAllByUserId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cart createCart(@RequestBody UUID userId) {
        return cartServices.createCart(userId);}

}
