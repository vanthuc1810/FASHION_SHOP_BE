package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponse {
    Integer idProduct;
    String description;
    String manufacturer;
    String name;
    String images;
    int discount;
    double price;
    boolean deleted;
    String unitStock;
    List<String> colors;
    List<String> sizes;
}
