package com.example.FashionShop.Dto.response.Revenue;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RevenueResponseItem {
    String status;
    int numRecord;
    List<Item> items;
}
