package com.example.FashionShop.Dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreationRequest {
    Integer idCategory;
    String description;
    String manufacturer;
    String images;
    String name;
    int discount;
    double price;
    String unitStock;
}
