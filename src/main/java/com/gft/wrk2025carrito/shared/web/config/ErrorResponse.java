package com.gft.wrk2025carrito.shared.web.config;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

}
