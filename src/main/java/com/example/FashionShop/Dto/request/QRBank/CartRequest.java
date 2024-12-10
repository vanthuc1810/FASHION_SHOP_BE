package com.example.FashionShop.Dto.request.QRBank;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    List<CartItemRequest> cart;
}
