package com.gft.wrk2025carrito.shopping_cart.infrastructure.persistence.entity;

import com.gft.wrk2025carrito.shopping_cart.domain.model.Cart.CartState;
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

    @Column(name = "id_User")
    private UUID userId;

    @Column(name = "TOTAL_WEIGHT")
    private double totalWeight;

    @ManyToOne
    @JoinColumn(name = "country_Tax_Id")
    private CountryTaxEntity countryTax;

    @ManyToOne
    @JoinColumn(name = "payment_Method_Id")
    private PaymentMethodEntity paymentMethod;

    @Column(name = "TOTAL_PRICE" ,columnDefinition = "DECIMAL(10,3)")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private CartState state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "CART_DETAILS", joinColumns = @JoinColumn(name = "cart_Id"))
    private List<CartDetailEntity> cartDetails;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable (name = "PROMOTIONS_CARTS", joinColumns = @JoinColumn(name = "CART_ID"))
    @Column(name = "ID_PROMOTION")
    private List<UUID> promotionIds;
}
