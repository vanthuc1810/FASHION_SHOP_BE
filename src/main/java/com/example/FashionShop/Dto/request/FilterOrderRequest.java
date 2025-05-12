package com.example.FashionShop.Dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterOrderRequest {
    List<String> status = new ArrayList<>();
    List<String> paymentMethod = new ArrayList<>();
    String typeFilter = "timeCreated";
    boolean acs = false;
}
