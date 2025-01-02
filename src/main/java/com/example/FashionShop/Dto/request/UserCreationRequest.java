package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationRequest {
    String userName;

    @Size(min = 8, message = "PASSWORD_SIZE_INVALID")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$", message = "PASSWORD_FORM_INVALID")
    String password;

    @NotBlank(message = "FIELD_NOTBLANK")
    String name;

    @Email(message = "EMAIL_INVALID")
    String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "PHONENUMER_INVALID")
    String phone;
    @NotBlank(message = "FIELD_NOTBLANK")
    @Size(min = 0, max = 100, message = "ADDRESS_INVALID")
    String address;

    String role;
}
