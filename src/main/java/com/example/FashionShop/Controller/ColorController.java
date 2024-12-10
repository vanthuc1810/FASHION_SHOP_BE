package com.example.FashionShop.Controller;

import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.ColorService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/color")
public class ColorController {
    ColorService colorService;

    @GetMapping()
    public ApiResponse getAllColor() {
        return colorService.getAllColor();
    }
}
