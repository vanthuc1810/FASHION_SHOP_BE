package com.example.FashionShop.Dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardItemCreationRequest {
    int quantity;
    Integer idProduct;
    String color;
    String size;
}
