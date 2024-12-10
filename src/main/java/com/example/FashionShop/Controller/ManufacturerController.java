package com.example.FashionShop.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/manufacturer")
public class ManufacturerController {
    ProductService productService;

    @GetMapping("")
    public ApiResponse getAllManufacturer() {
        return productService.getAllManufacturer();
    }
}
