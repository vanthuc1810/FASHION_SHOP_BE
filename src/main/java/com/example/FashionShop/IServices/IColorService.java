package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ColorCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;

public interface IColorService {
    public ApiResponse createColor(ColorCreationRequest request);

    public ApiResponse getColor(String nameColor);

    public ApiResponse getAllColor();
}
