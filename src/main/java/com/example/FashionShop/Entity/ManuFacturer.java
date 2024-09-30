package com.example.FashionShop.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ManuFacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String idManuFacturer;
    String name;

    @OneToMany(mappedBy = "manufacturer")
    List<Product> products;
}
