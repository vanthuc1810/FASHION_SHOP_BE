package com.example.FashionShop.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SalesOrderStatus {
    CREATED("Tạo hóa đơn thành công"),
    PENDING_PAYMENT("Chờ thanh toán!"),
    PENDING_SUCCESS("Thanh toán thành công!"),
    PENDING_FALLD("Thanh toán thất bại!"),
    CONFIRM("Hóa đơn đã được xác nhận, vui lòng xác nhận khi nhận được hàng!"),
    COMPLETE("Hoàn thành!"),
    CANCLE("Hủy"),
    ;

    String message;
}
