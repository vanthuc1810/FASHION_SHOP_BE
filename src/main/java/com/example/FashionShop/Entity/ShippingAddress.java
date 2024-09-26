package com.example.FashionShop.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String idShippingAddress;
    String address;
    String city;
    String state;
    String zipcode;
    String country;

    @Builder.Default
    boolean defaultAddress = false;

    @OneToMany(mappedBy = "shippingAddress")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<SalesOrder> salesOrders = new ArrayList<>();

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @JoinColumn(name = "id_user")
    User user;
}
