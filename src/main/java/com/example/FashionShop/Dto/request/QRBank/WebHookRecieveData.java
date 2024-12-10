package com.example.FashionShop.Dto.request.QRBank;

import java.util.Objects;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebHookRecieveData {
    String code;
    String desc;
    boolean success;
    Objects data;
    String signature;
}
