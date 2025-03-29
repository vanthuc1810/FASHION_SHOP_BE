package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherUpdateRequest {
    @Min(value = 0, message = "DISCOUNT_INVALID")
    @Max(value = 100, message = "DISCOUNT_INVALID")
    Float discountPercentage;
    @Min(value = 0, message = "DISCOUNT_AMOUNT_INVALID")
    Float discountAmount;
    @Min(value = 0, message = "MIN_ORDER_VALUE_INVALID")
    Float minOrderValue;

    @FutureOrPresent(message = "FUTURE_OR_PRESENT_INVALID")
    LocalDate expirationDate;

    @FutureOrPresent(message = "FUTURE_OR_PRESENT_INVALID")
    LocalDate startDate;

//     Kiểm tra xem startDate có nhỏ hơn expirationDate không
    @AssertTrue(message = "START_DATE_MUST_BE_BEFORE_EXPIRATION_DATE")
    public boolean isStartDateBeforeExpirationDate() {
        if(startDate != null && expirationDate != null)
        {
            return startDate.isBefore(expirationDate);
        }
        return true;
    }
}
