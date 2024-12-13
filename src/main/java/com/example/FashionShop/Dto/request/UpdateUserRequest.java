package com.example.FashionShop.Dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @NotBlank(message = "FIELD_NOTBLANK")
    String name;
    @Email(message = "EMAIL_INVALID")
    String email;
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "PHONENUMER_INVALID")
    String phone;
    @NotBlank(message = "FIELD_NOTBLANK")
    @Size(min = 0, max = 100, message = "ADDRESS_INVALID")
    String address;
}
