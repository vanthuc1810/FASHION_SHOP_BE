package com.example.FashionShop.Dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VoucherResponse {
    Integer idVoucher;
    String code;
    float discountPercentage;
    float discountAmount;
    float minOrderValue;
    LocalDate expirationDate;
    LocalDate startDate;
    boolean isActive;
}
