package com.gft.wrk2025carrito.shared.web.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponseStoresErrorMessage() {
        String errorMessage = "Something went wrong";
        ErrorResponse response = new ErrorResponse(errorMessage);

        assertEquals(errorMessage, response.getError());
    }

}