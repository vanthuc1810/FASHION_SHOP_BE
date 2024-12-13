package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesOrderCreationRequest {
    @NotNull(message = "NULL_VALUE")
    Integer idCard;
    @NotNull(message = "NULL_VALUE")
    Integer idShippingAddress;
    String paymentMethod;
}
