package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopUpWalletRequest {
    @Min(value = 2000, message = "TOPUP_WALLET_INVALID")
    int topUpWalletValue;
}
