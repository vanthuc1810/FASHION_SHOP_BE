package com.example.FashionShop.Dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterUserRequest {
    List<String> roles = new ArrayList<>();
    List<Boolean> currentStatus = new ArrayList<>();
    List<Boolean> isDeleteds = new ArrayList<>();
}
