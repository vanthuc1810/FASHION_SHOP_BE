package com.example.FashionShop.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.SizeCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.SizeResponse;
import com.example.FashionShop.IServices.ISizeService;
import com.example.FashionShop.Repository.SizeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SizeService implements ISizeService {
    SizeRepository sizeRepository;

    @Override
    public ApiResponse<SizeResponse> createSize(SizeCreationRequest request) {
        return null;
    }

    @Override
    public ApiResponse<SizeResponse> getSize(String nameSize) {
        return null;
    }

    @Override
    public ApiResponse getAllSize() {
        List<String> listSize = sizeRepository.findAllSize();
        return new ApiResponse().builder().results(listSize).build();
    }
}
