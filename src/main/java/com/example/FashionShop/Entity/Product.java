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
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String idProduct;


    String description;
    String manufacturer;
    String name;
    String images;
    int discount;
    double price;
    @Builder.Default
    boolean deleted = false;
    String unitStock;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @JoinColumn(name = "id_category")
    Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    List<CardItem> cardItems = new ArrayList<>();

}
