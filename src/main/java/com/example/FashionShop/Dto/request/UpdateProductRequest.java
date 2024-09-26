package com.example.FashionShop.Dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    String idCategory;
    String description;
    String manufacturer;
    String name;
    int discount;
    double price;
    String unitStock;
}
