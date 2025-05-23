package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity;

import com.gft.wrk2025carrito.shopping_cart.domain.model.CartState;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Generated
@Entity
@Table(name = "CARTS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "idUser")
    private UUID userId;

    private double totalWeight;

    @ManyToOne
    @JoinColumn(name = "countryTaxId")
    private CountryTaxEntity countryTaxId;

    @ManyToOne
    @JoinColumn(name = "paymentMethodId")
    private PaymentMethodEntity paymentMethodId;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private CartState state;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)

    private Date updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)

    @JoinTable(name = "CART_DETAILS", joinColumns = @JoinColumn(name = "cartId"))

    private List<CartDetailEntity> cartDetails;

    @ElementCollection(fetch = FetchType.EAGER)

    @JoinTable (name = "PROMOTIONS_CARTS", joinColumns = @JoinColumn(name = "cartId"))

    private List<UUID> promotionIds;

}