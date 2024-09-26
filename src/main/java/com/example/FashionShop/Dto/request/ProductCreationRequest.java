package com.example.FashionShop.Dto.request;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreationRequest {
    String idCategory;
    String description;
    String manufacturer;
    String images;
    String name;
    int discount;
    double price;
    String unitStock;
}
