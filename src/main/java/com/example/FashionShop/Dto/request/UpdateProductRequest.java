package com.example.FashionShop.Dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProductRequest {
    Integer idProduct;
    public Integer idCategory = null;
    @Size(min = 0, max = 400, message = "SIZE_DESCRIPTION_INVALID")
    String description;
    @Size(min = 0, max = 20, message = "SIZE_MANUFACTURER_INVALID")
    @Pattern(
            regexp = "^[\\p{L}0-9][\\p{L}\\p{M}0-9 '\\-_/&.]{1,98}[\\p{L}0-9]$",
            message = "MANUFRACTURER_INVALID"
    )
    String manufacturer;
    @Size(min = 0, max = 60, message = "SIZE_NAME_INVALID")
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
    String unitStock = "VND";
    @Min(value = 0, message = "QUANITTY_INVALID")
    @Max(value = 2000, message = "QUANITTY_INVALID")
    Integer quantity;
    String images;

    List<String> colors;
    List<String> sizes;

}
