package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardItemResponse {
    Integer idCardItem;
    double price;
    int quantity;
    ProductResponse product;
    String color;
    String size;
}
