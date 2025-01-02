package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RevenueResponse {
    List<SaleOrderResponse> orders;
    Long totalOrder;
    float totalPrice;
}
