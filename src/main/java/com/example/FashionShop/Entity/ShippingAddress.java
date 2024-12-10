package com.example.FashionShop.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idShippingAddress;
    String address;
    @Builder.Default
    boolean defaultAddress = false;

    @JsonIgnore
    @OneToMany(mappedBy = "shippingAddress")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<SalesOrder> salesOrders = new ArrayList<>();

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @JoinColumn(name = "id_user")
    User user;
}
