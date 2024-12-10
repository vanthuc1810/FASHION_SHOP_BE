package com.example.FashionShop.Enum;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOTFOUND(1001, "Tài khoản không tồn tại", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    PRODUCT_NOTFOUND(1003, "Sản phẩm không tồn tại", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1004, "Tài khoản không có quyền truy cập", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1005, "Tài khoản chưa xác thực", HttpStatus.UNAUTHORIZED),
    REVIEW_NOTFOUND(1006, "Không tìm thấy review", HttpStatus.BAD_REQUEST),
    CATEGORY_NOTFOUND(1007, "Không tìm thấy danh mục tương ứng", HttpStatus.BAD_REQUEST),
    CARD_NOTFOUND(1008, "Không tìm thấy giỏ hàng", HttpStatus.BAD_REQUEST),
    ADDRESS_NOTFOUND(1009, "Không tìm thấy địa chỉ tương ứng", HttpStatus.BAD_REQUEST),
    SALES_ORDER_NOTFOUND(1010, "Không tìm thấy hóa đơn tương ứng", HttpStatus.BAD_REQUEST),
    COMFIRM_FAILD(1011, "Xác nhận hóa đơn thất bại", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE_INVALID(1012, "Mật khẩu cần phải có ít nhất 8 ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_FORM_INVALID(1013, "Mật khẩu không hợp lệ", HttpStatus.BAD_REQUEST),
    PHONENUMER_INVALID(1013, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1013, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOTFOUND(1014, "Không tìm thấy giao dịch", HttpStatus.BAD_REQUEST),

    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
