package com.example.FashionShop.Controller;

import java.io.UnsupportedEncodingException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.VNPAYService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPAYController {
    VNPAYService vnpayService;

    @PostMapping("/vnpay")
    public ApiResponse vnpay() throws UnsupportedEncodingException {
        return new ApiResponse().builder().results(vnpayService.createPayment()).build();
    }
}
