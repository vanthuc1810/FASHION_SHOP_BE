package com.example.FashionShop.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String idSalesOrder;

    String status;
    String paymentMethod;

    @OneToOne
    @JoinColumn(name = "id_card")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Card card;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_user")
    User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_shipping_address")
    ShippingAddress shippingAddress;
}
