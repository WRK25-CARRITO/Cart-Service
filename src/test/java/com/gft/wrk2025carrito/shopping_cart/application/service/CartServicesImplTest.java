package com.gft.wrk2025carrito.shopping_cart.application.service;

import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartEntityRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServicesImplTest {

    @Mock
    private CartEntityRepositoryImpl repository;

    @InjectMocks
    private CartServicesImpl service;

    @Test
    void should_throw_exception_when_id_is_null() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(null));
    }

    @Test
    void should_throw_exception_when_cart_not_exists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> service.delete(id));
    }

    @Test
    void should_delete_cart_when_exists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}