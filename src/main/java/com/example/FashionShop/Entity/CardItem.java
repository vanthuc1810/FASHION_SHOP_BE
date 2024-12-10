package com.example.FashionShop.Entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idCardItem;

    int quantity;
    double price;
    String color;
    String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_card")
    Card card;
}
