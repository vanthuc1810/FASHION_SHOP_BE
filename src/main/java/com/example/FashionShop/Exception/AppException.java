package com.example.FashionShop.Exception;

import com.example.FashionShop.Enum.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
}
