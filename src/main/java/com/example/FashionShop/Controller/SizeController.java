package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.SizeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/size")
public class SizeController {
    SizeService sizeService;

    @GetMapping()
    public ApiResponse getAllSize()
    {
        return sizeService.getAllSize();
    }
}
