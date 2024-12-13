package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
    @Size(min = 2, max = 20, message = "SIZE_CATEGORY_INVALID")
    @Pattern(
            regexp = "^[A-Z][^\\d]+$",
            message = "CATEGORY_INVALID"
    )
    String name;
}
