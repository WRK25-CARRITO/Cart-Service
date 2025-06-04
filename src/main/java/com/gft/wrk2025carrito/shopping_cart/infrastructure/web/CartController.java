package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import com.gft.wrk2025carrito.shopping_cart.application.dto.CartDTO;
import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.domain.services.CartServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartServices cartServices;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cart> getAll() {
        return cartServices.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cart getById(@PathVariable UUID id) {
        return cartServices.getById(id);
    }

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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID id, @RequestBody Map<Long,Integer> cartProducts) {
        cartServices.update(id, cartProducts);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cart createCart(@RequestParam UUID userId) {
        return cartServices.createCart(userId);
    }

    @PutMapping("/order/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cart updateState(@PathVariable UUID id, @RequestBody CartDTO cartDTO) {
        return cartServices.updateState(id, cartDTO);
    }

}
