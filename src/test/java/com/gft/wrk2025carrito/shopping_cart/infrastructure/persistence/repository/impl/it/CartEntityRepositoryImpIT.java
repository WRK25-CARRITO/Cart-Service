package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.it;

import com.gft.wrk2025carrito.shopping_cart.domain.model.cart.Cart;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity.CartEntity;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.factory.CartFactory;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CartDetailMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.CountryTaxMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.mapper.PaymentMethodMapper;
import com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.repository.impl.CartEntityRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
        CartEntityRepositoryImpl.class,
        CartFactory.class,
        CountryTaxMapper.class,
        PaymentMethodMapper.class,
        CartDetailMapper.class
})
@ActiveProfiles("test")
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

        List<Cart> result = cartEntityRepository.findByUserId(userId);

        assertFalse(result.isEmpty(), "Expected carts to be found for user ID");
    }

    @Test
    void findById_WhenCartsExist() {
        UUID cartId = UUID.fromString("4d82b684-7131-4ba4-864d-465fc290708b");

        Cart result = cartEntityRepository.findById(cartId);

        assertNotNull(result);
    }



    @Test
    void deleteByUserId_WhenCartsExist_RemovesCarts() {
        UUID userId = UUID.fromString("b96124a9-69a6-4859-acc7-5708ab07cd80");

        assertFalse(cartEntityRepository.findByUserId(userId).isEmpty(), "Should have carts before deletion");

        cartEntityRepository.deleteAllByUserId(userId);

        assertTrue(cartEntityRepository.findByUserId(userId).isEmpty(), "Carts should be removed after deletion");
    }

    @Test
    void existsByUserIdAndCartState(){
        UUID userId = UUID.fromString("2f05a6f9-87dc-4ea5-a23c-b05265055334");
        boolean exists = cartEntityRepository.cartExistsByUserIdAndStateActive(userId);
        assertTrue(exists);
    }


}

