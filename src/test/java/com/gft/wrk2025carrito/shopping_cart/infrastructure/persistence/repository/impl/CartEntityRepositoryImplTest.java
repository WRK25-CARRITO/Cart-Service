package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(CartEntityRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts= {"/data/h2/schema.sql", "/data/h2/data.sql"})
class CartEntityRepositoryImplTest{

    @Autowired
    CartEntityRepositoryImpl cartEntityRepository;

    @Test
    void deleteById_WhenCartExists_RemovesCart() {
        UUID cartId = UUID.fromString("4d82b684-7131-4ba4-864d-465fc290708b");

        assertTrue(cartEntityRepository.existsById(cartId));

        cartEntityRepository.deleteById(cartId);

        assertFalse(cartEntityRepository.existsById(cartId));
    }


    @Test
    void existsById_WhenCartExists_ReturnsTrue() {
        UUID existingId = UUID.fromString("4d82b684-7131-4ba4-864d-465fc290708b");

        boolean exists = cartEntityRepository.existsById(existingId);

        assertTrue(exists);
    }

    @Test
    void existsById_WhenCartDoesNotExist_ReturnsFalse() {
        UUID nonexistentId = UUID.randomUUID();

        boolean exists = cartEntityRepository.existsById(nonexistentId);

        assertFalse(exists);
    }

}

