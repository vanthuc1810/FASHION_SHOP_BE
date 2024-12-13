package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardItemCreationRequest {
    @Min(value = 1, message = "QUANTITY_NOTVALID")
    @Max(value = 1000, message = "QUANTITY_NOTVALID")
    @NotNull(message = "FIELD_NOTBLANK")
    int quantity;
    @NotNull
    Integer idProduct;
    @Size(min = 4, max = 8, message = "SIZE_COLOR_INVALID")
    String color;
    @Size(min = 4, max = 8, message = "SIZE_SIZE_INVALID")
    String size;
}
