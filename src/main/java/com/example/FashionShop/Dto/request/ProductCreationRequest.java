package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreationRequest {
    @NotNull(message = "NULL_VALUE")
    Integer idCategory;
    @Size(min = 0, max = 400, message = "SIZE_DESCRIPTION_INVALID")
    String description;
    @Size(min = 0, max = 20, message = "SIZE_MANUFACTURER_INVALID")
    @Pattern(
            regexp = "^[\\p{L}0-9][\\p{L}\\p{M}0-9 '\\-_/&.]{1,98}[\\p{L}0-9]$",
            message = "NAME_PRODUCT_INVALID"
    )
    String manufacturer;
    @NotBlank
    String images;
    @Size(min = 0, max = 100, message = "SIZE_NAME_INVALID")
    @Pattern(
            regexp = "^[\\p{L}0-9][\\p{L}\\p{M}0-9 '\\-_/&.]{1,98}[\\p{L}0-9]$",
            message = "NAME_PRODUCT_INVALID"
    )
    String name;
    @Min(value = 0, message = "DISCOUNT_INVALID")
    @Max(value = 100, message = "DISCOUNT_INVALID")
    int discount;
    @Min(value = 0, message = "PRICE_INVALID")
    double price;
    @Size(min = 2, max = 4, message = "UNITSTOCK_INVALID")
    String unitStock;

    @Min(value = 1, message = "DISCOUNT_INVALID")
    Integer quantity;

    @NotEmpty(message = "COLOR_NOT_EMPTY")
    List<String> colors;

    @NotEmpty(message = "SIZE_NOT_EMPTY")
    List<String> sizes;

}
