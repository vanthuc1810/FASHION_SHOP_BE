package com.example.FashionShop.Dto.request;

import com.example.FashionShop.Enum.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderCreationRequest {
    String idCard;
    String idShippingAddress;
    String paymentMethod;
}
