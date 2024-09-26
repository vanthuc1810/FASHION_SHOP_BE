package com.example.FashionShop.Enum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum PaymentMethod {
    WALLET("Wallet"),
    BANK_TRANSFER("Bank Transfer"),
    CASH("Cash")
    ;
    String name;
}
