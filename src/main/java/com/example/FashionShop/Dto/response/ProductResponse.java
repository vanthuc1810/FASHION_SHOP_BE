package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponse {
    String idProduct;
    String description;
    String manufacturer;
    String name;
    String images;
    int discount;
    double price;
    boolean deleted;
    String unitStock;
}
