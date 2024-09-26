package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SaleOrderResponse {
    String idSalesOrder;
    String idCard;
    String idShippingAddress;
    String idUser;
    String status;
}
