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
    CATEGORY_EXISTED(1022, "Danh mục đã tồn tại", HttpStatus.BAD_REQUEST),
    CARD_NOTFOUND(1008, "Không tìm thấy giỏ hàng", HttpStatus.BAD_REQUEST),
    ADDRESS_NOTFOUND(1009, "Không tìm thấy địa chỉ tương ứng", HttpStatus.BAD_REQUEST),
    SALES_ORDER_NOTFOUND(1010, "Không tìm thấy hóa đơn tương ứng", HttpStatus.BAD_REQUEST),
    COMFIRM_FAILD(1011, "Xác nhận hóa đơn thất bại", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE_INVALID(1012, "Mật khẩu cần phải có ít nhất 8 ký tự bao gồm cả số và kí tự đặc biệt", HttpStatus.BAD_REQUEST),
    PASSWORD_FORM_INVALID(1013, "Mật khẩu không hợp lệ", HttpStatus.BAD_REQUEST),
    PHONENUMER_INVALID(1013, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1013, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOTFOUND(1014, "Không tìm thấy giao dịch", HttpStatus.BAD_REQUEST),
    USERNAME_ISBLANK(1015, "UserName không được để trống", HttpStatus.BAD_REQUEST),
    FIELD_NOTBLANK(1016, "Vui lòng điền đầy đủ thông tin!", HttpStatus.BAD_REQUEST),
    CARD_NOT_EMPTY(1017, "Giỏ hàng trống!", HttpStatus.BAD_REQUEST),
    QUANTITY_NOTVALID(1018, "Số lượng không được quá 1000 và nhỏ hơn 1", HttpStatus.BAD_REQUEST),
    SIZE_COLOR_INVALID(1019, "Color không được quá 8 kí tự và ít hơn 4", HttpStatus.BAD_REQUEST),
    SIZE_SIZE_INVALID(1019, "Size không được quá 8 kí tự và ít hơn 4", HttpStatus.BAD_REQUEST),
    SIZE_CATEGORY_INVALID(1020, "Tên danh mục cần ít nhất 2 kí tự và không được quá 20 kí tự", HttpStatus.BAD_REQUEST),
    CATEGORY_INVALID(1021, "Category cần bắt đầu bằng chữ hoa và không chứa số", HttpStatus.BAD_REQUEST),
    COLOR_NOT_EMPTY(1022, "Không được để trông color", HttpStatus.BAD_REQUEST),
    SIZE_DESCRIPTION_INVALID(1023, "Mô tả không được quá 400 ký tự", HttpStatus.BAD_REQUEST),
    SIZE_MANUFACTURER_INVALID(1024, "Tên nhà sản xuất không được quá 20 ký tự", HttpStatus.BAD_REQUEST),
    MANUFRACTURER_INVALID(1025, "Tên nhà sản xuất phải bắt đầu bằng 1 kí tự viết hoa và không chứa số", HttpStatus.BAD_REQUEST),
    SIZE_NAME_INVALID(1026, "Tên sản phẩm không được quá 20 ký tự", HttpStatus.BAD_REQUEST),
    NAME_PRODUCT_INVALID(1027, "Tên nhà sản xuất phải bắt đầu bằng 1 kí tự viết hoa và không chứa số", HttpStatus.BAD_REQUEST),
    DISCOUNT_INVALID(1028, "Discount phải có giá trị trong khoảng 0 - 100", HttpStatus.BAD_REQUEST),
    PRICE_INVALID(1029, "Giá sản phẩm không được âm", HttpStatus.BAD_REQUEST),
    UNITSTOCK_INVALID(1030, "Unitstock không được quá 2 - 4 kí tự", HttpStatus.BAD_REQUEST),
    STAR_INVALID(1031, "Star phải có giá trị trong khoảng 0 - 5", HttpStatus.BAD_REQUEST),
    SIZE_REVIEW_INVALID(1032, "Đánh giá không được quá 400 ký tự", HttpStatus.BAD_REQUEST),
    NULL_VALUE(1033, "Giá trị không được phép null", HttpStatus.BAD_REQUEST),
    ADDRESS_INVALID(1034, "Địa chỉ không được quá 100 ký tự", HttpStatus.BAD_REQUEST),
    SIZE_NOT_EMPTY(1035, "Không được để trông size", HttpStatus.BAD_REQUEST),
    TOPUP_WALLET_INVALID(1036, "Giá trị phải ít nhất 2000 VND", HttpStatus.BAD_REQUEST),

    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
