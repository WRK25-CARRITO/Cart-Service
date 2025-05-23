package com.gft.wrk2025carrito.shopping_cart.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @PutMapping()
    public ResponseEntity<?> findAll() {
        String s = "hola caracola2";
        return new ResponseEntity<>(s, HttpStatus.OK);

    }
}
