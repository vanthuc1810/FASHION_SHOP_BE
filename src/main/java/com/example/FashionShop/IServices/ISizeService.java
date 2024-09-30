package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.SizeCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.SizeResponse;

public interface ISizeService {
    public ApiResponse<SizeResponse> createSize(SizeCreationRequest request);
    public ApiResponse<SizeResponse> getSize(String nameSize);
    public ApiResponse getAllSize();
}
