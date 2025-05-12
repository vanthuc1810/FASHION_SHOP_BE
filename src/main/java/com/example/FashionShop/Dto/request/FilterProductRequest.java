package com.example.FashionShop.Dto.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterProductRequest {
    List<String> sizes = new ArrayList<>();
    List<String> colors = new ArrayList<>();
    List<Integer> idCategorys = new ArrayList<>();
    List<String> manufacturers = new ArrayList<>();
    List<Double> prices = new ArrayList<>(Arrays.asList(0.0, 1000000.0));
    List<Boolean> isDeleted = new ArrayList<>(List.of(true, false));

}
