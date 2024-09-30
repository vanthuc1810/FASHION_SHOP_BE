package com.example.FashionShop.Dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ColorResponse {
    String nameColor;
    String idProducts;
}
