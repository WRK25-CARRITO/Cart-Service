package com.gft.wrk2025carrito.shared.web.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleIllegalArgument_returnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");
        var response = handler.handleIllegalArgument(ex);
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("message").toString().contains("Invalid input"));
    }

    @Test
    void handleIllegalState_returnsNotFound() {
        IllegalStateException ex = new IllegalStateException("Not found");
        var response = handler.handleIllegalState(ex);
        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().get("message").toString().contains("Not found"));
    }

    @Test
    void handleGeneric_returnsInternalServerError() {
        Exception ex = new Exception("Something went wrong");
        var response = handler.handleGeneric(ex);
        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().get("message").toString().contains("Something went wrong"));
    }

    @Test
    void buildBody_whenMessageIsNull_setsDefaultMessage() {
        var response = handler.handleGeneric(new Exception());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error", response.getBody().get("message"));
    }

    @Test
    void handleHttpMessageNotReadable_returnsBadRequest() {
        var request = new ServletWebRequest(new MockHttpServletRequest());
        var response = handler.handleHttpMessageNotReadable(new HttpMessageNotReadableException("error"), new HttpHeaders(), HttpStatusCode.valueOf(400), request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());

        Object body = response.getBody();
        assertInstanceOf(ErrorResponse.class, body);
        assertEquals("JSON object cannot be deserialized.", ((ErrorResponse) body).getError());

    }

    @Test
    void handleMethodArgumentNotValid_returnsBadRequest() {
        var request = new ServletWebRequest(new MockHttpServletRequest());
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        var response = handler.handleMethodArgumentNotValid(
                ex,
                new HttpHeaders(),
                HttpStatusCode.valueOf(400),
                request
        );

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());

        Object body = response.getBody();
        assertInstanceOf(ErrorResponse.class, body);
        assertEquals("Request parameter validation failed.", ((ErrorResponse) body).getError());

    }

    @Test
    void handleTypeMismatch_returnsBadRequest_withExpectedMessage() {
        var request = new ServletWebRequest(new MockHttpServletRequest());

        TypeMismatchException ex = new TypeMismatchException("123", Integer.class);
        ex.initPropertyName("id");

        var response = new GlobalExceptionHandler().handleTypeMismatch(
                ex,
                new HttpHeaders(),
                HttpStatusCode.valueOf(400),
                request
        );

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertInstanceOf(ErrorResponse.class, response.getBody());

        ErrorResponse body = (ErrorResponse) response.getBody();
        assertNotNull(body.getError());
        assertTrue(body.getError().contains("Parameter [id] returned [123]"));
        assertTrue(body.getError().contains("Expected value: [Integer]"));
    }
}
