package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.ColorCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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
