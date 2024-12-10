package com.example.FashionShop.Dto.request.QRBank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentLinkRequest {
    Integer idSalesOrder;
}
