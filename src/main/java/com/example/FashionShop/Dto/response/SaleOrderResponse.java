package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SaleOrderResponse {
    Integer idSalesOrder;
    Integer idCard;
    Integer idShippingAddress;
    Integer idUser;
    String status;
    String paymentMethod;
    LocalDateTime timeFinished;
}
