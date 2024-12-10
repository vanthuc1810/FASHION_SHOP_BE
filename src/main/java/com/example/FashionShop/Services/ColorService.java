package com.example.FashionShop.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.ColorCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.IServices.IColorService;
import com.example.FashionShop.Repository.ColorRepository;
import com.example.FashionShop.Repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ColorService implements IColorService {
    ColorRepository colorRepository;
    ProductRepository productRepository;

    @Override
    public ApiResponse createColor(ColorCreationRequest request) {
        return null;
    }

    @Override
    public ApiResponse getColor(String nameColor) {
        return null;
    }

    @Override
    public ApiResponse getAllColor() {
        List<String> listColor = colorRepository.findAllColor();
        return new ApiResponse().builder().results(listColor).build();
    }
}
