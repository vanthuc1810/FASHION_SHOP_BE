package com.example.FashionShop.Entity;

import jakarta.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idSalesOrder;

    String status;
    String paymentMethod;
    LocalDateTime timeCreated;
    LocalDateTime timeFinished = null;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_card")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_user")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_shipping_address")
    ShippingAddress shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_voucher")
    Voucher voucher;

}
