package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.it;

import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.PaymentMethodMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartEntityRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({
        CartEntityRepositoryImpl.class,
        CartFactory.class,
        CountryTaxMapper.class,
        PaymentMethodMapper.class,
        CartDetailMapper.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
//@Sql(scripts= {"/data/h2/schema.sql", "/data/h2/data.sql"})
class CartEntityRepositoryImpIT {

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

    @Test
    void findByUserId_WhenCartsExist_ReturnsList() {
        UUID userId = UUID.fromString("b96124a9-69a6-4859-acc7-5708ab07cd80");

        var result = cartEntityRepository.findByUserId(userId);

        assertFalse(result.isEmpty(), "Expected carts to be found for user ID");
    }

    @Test
    void deleteByUserId_WhenCartsExist_RemovesCarts() {
        UUID userId = UUID.fromString("b96124a9-69a6-4859-acc7-5708ab07cd80");

        assertFalse(cartEntityRepository.findByUserId(userId).isEmpty(), "Should have carts before deletion");

        cartEntityRepository.deleteAllByUserId(userId);

        assertTrue(cartEntityRepository.findByUserId(userId).isEmpty(), "Carts should be removed after deletion");
    }

}

