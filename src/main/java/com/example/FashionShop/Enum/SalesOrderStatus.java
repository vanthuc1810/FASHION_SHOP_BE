package com.example.FashionShop.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SalesOrderStatus {
    CREATED,
    PENDING_PAYMENT,
    IN_PROGRESS,
    COMPLETE,
    CANCLE,
    ;
}
