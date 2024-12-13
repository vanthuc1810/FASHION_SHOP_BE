package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreationRequest {
    @Size(min = 0, max = 400, message = "SIZE_REVIEW_INVALID")
    String comment;
    @Min(value = 0, message = "STAR_INVALID")
    @Max(value = 5, message = "STAR_INVALID")
    int star;
    @NotNull(message = "NULL_VALUE")
    Integer idProduct;
}
