package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CartState;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder( toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CARTS")
public class CartEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "ID_USER", nullable = false)
    private UUID userId;

    @Column(name = "TOTAL_PRICE", columnDefinition = "DECIMAL(10,3)")
    private BigDecimal totalPrice;

    @Column(name = "TOTAL_WEIGHT")
    private double totalWeight;

    @Column(name = "COUNTRY_TAX_ID")
    private UUID countryTaxId;

    @Column(name = "PAYMENT_METHOD_ID")
    private UUID paymentMethodId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private CartState state;

    @ElementCollection(fetch =  FetchType.EAGER)
    @JoinTable(name = "CART_DETAILS", joinColumns = @JoinColumn(name="CART_ID"))
    private List<CartDetailEntity> cartDetails;

    @ElementCollection(fetch =  FetchType.EAGER)
    @JoinTable(name = "CART_DETAILS", joinColumns = @JoinColumn(name="CART_ID"))
    private List<UUID> promotionsId;
}
