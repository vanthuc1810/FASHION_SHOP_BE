package com.example.FashionShop.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Color {
    @Id
    String nameColor;

    @ManyToMany(mappedBy = "colors")
    @JsonIgnore
    List<Product> products;
}
