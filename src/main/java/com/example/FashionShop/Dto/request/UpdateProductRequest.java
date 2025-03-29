package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    @NotNull(message = "NULL_VALUE")
    public Integer idCategory;
    @Size(min = 0, max = 400, message = "SIZE_DESCRIPTION_INVALID")
    String description;
    @Size(min = 0, max = 20, message = "SIZE_MANUFACTURER_INVALID")
    @Pattern(
            regexp = "^[A-Z][^\\d]+$",
            message = "MANUFRACTURER_INVALID"
    )
    String manufacturer;
    @Size(min = 0, max = 20, message = "SIZE_NAME_INVALID")
    @Pattern(
            regexp = "^[A-Z][^\\d]+$",
            message = "NAME_PRODUCT_INVALID"
    )
    String name;
    @Min(value = 0, message = "DISCOUNT_INVALID")
    @Max(value = 100, message = "DISCOUNT_INVALID")
    int discount;
    @Min(value = 0, message = "PRICE_INVALID")
    double price;
    @Size(min = 2, max = 4, message = "UNITSTOCK_INVALID")
    String unitStock = "VND";
}
