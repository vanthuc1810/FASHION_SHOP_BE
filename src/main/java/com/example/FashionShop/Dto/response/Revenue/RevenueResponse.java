package com.example.FashionShop.Dto.response.Revenue;

import com.example.FashionShop.Dto.response.SaleOrderResponse;
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
    List<RevenueResponseItem> data;
    Long totalOrder;
    float totalPrice;
}
