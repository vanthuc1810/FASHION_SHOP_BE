package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String idUser;
    String name;
    String email;
    String phone;
    String address;
    String userName;
    String password;
    String role;
    float wallet;
    boolean isAvaialbe;
}
